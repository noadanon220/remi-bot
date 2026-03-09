package com.danono.remi_bot.service;

import com.danono.remi_bot.model.IntentResult;
import com.danono.remi_bot.model.MessageIntent;
import com.danono.remi_bot.model.Reminder;
import com.danono.remi_bot.model.ReminderExtractionResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final ChatClient chatClient;
    private final ReminderService reminderService;

    public MessageService(ChatClient.Builder chatClientBuilder, ReminderService reminderService, TwilioService twilioService) {
        this.chatClient = chatClientBuilder.build();
        this.reminderService = reminderService;
    }

    public String processIncomingMessage(String sender, String messageBody) {

        System.out.println("Processing message...");
        System.out.println("Sender: " + sender);
        System.out.println("Message: " + messageBody);

        IntentResult intentResult = classifyIntent(messageBody);

        System.out.println("Detected intent: " + intentResult.getIntent());

        return switch (intentResult.getIntent()) {
            case CHAT -> handleChat(messageBody);
            case SAVE_MEMORY -> handleSaveMemory(intentResult.getExtractedText());
            case CREATE_REMINDER -> handleCreateReminder(sender, intentResult.getExtractedText());
        };
    }

    private IntentResult classifyIntent(String messageBody) {

        String classificationPrompt = """
                Classify the user's message into exactly one of these categories:
                CHAT
                SAVE_MEMORY
                CREATE_REMINDER

                Rules:
                - CHAT: general conversation, questions, greetings, casual talk
                - SAVE_MEMORY: personal diary entries, feelings, events, things the user wants remembered
                - CREATE_REMINDER: requests to be reminded about something in the future

                Respond with only one word:
                CHAT
                SAVE_MEMORY
                CREATE_REMINDER
                """;

        String result = chatClient.prompt()
                .system(classificationPrompt)
                .user(messageBody)
                .call()
                .content();

        String cleaned = result == null ? "" : result.trim().toUpperCase();

        MessageIntent intent;
        try {
            intent = MessageIntent.valueOf(cleaned);
        } catch (IllegalArgumentException ex) {
            intent = MessageIntent.CHAT;
        }

        return new IntentResult(intent, messageBody);
    }

    private String handleChat(String messageBody) {
        return chatClient.prompt()
                .system("""
                        You are a helpful personal WhatsApp diary assistant.
                        Reply warmly, briefly, and in the same language as the user.
                        """)
                .user(messageBody)
                .call()
                .content();
    }

    private String handleSaveMemory(String messageBody) {
        return "שמרתי את זה ביומן שלך: " + messageBody;
    }

    private String handleCreateReminder(String sender, String messageBody) {

        ReminderExtractionResult result = extractReminderDetails(messageBody);

        Reminder reminder = new Reminder(
                sender,
                messageBody,
                result.getTask(),
                result.getTime()
        );

        reminderService.saveReminder(reminder);

        return "שמרתי תזכורת: " + result.getTask() +
                (result.getTime() != null ? " בזמן: " + result.getTime() : "");
    }

    private ReminderExtractionResult extractReminderDetails(String messageBody) {

        String prompt = """
                Extract reminder details from the user's message.

                Return JSON with this exact structure:
                {
                  "task": "...",
                  "time": "..."
                }

                Rules:
                - "task" should contain only the action/task itself.
                - "time" should be in ISO LocalDateTime format: yyyy-MM-ddTHH:mm
                - If the user gives only a time for today, convert it to today's full date and time.
                - If the user says "tomorrow", convert it to tomorrow's date.
                - If no time is specified, return null for time.
                - Return only valid JSON, nothing else.

                Current date and time:
                """ + java.time.LocalDateTime.now() + """

                Examples:
                User: תזכיר לי לשתות מים היום ב-18:00
                Response:
                {"task":"לשתות מים","time":"2026-03-08T18:00"}

                User: תזכיר לי להתקשר לוטרינרית מחר ב-12:45
                Response:
                {"task":"להתקשר לוטרינרית","time":"2026-03-09T12:45"}
                """;

        String response = chatClient.prompt()
                .system(prompt)
                .user(messageBody)
                .call()
                .content();

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response, ReminderExtractionResult.class);
        } catch (Exception e) {
            System.out.println("Failed to parse reminder JSON: " + response);
            return new ReminderExtractionResult(messageBody, null);
        }
    }
}
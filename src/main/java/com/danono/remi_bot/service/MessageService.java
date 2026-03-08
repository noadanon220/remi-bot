package com.danono.remi_bot.service;

import com.danono.remi_bot.model.IntentResult;
import com.danono.remi_bot.model.MessageIntent;
import com.danono.remi_bot.model.Reminder;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final ChatClient chatClient;
    private final ReminderService reminderService;

    public MessageService(ChatClient.Builder chatClientBuilder, ReminderService reminderService) {
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
        Reminder reminder = new Reminder(sender, messageBody, messageBody);
        reminderService.saveReminder(reminder);

        return "שמרתי תזכורת: " + messageBody;
    }
}
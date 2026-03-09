package com.danono.remi_bot.service;

import com.danono.remi_bot.model.Reminder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderScheduler {

    private final ReminderService reminderService;
    private final TwilioService twilioService;

    public ReminderScheduler(ReminderService reminderService, TwilioService twilioService) {
        this.reminderService = reminderService;
        this.twilioService = twilioService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkReminders() {

        List<Reminder> reminders = reminderService.getAllReminders();
        LocalDateTime now = LocalDateTime.now();

        for (Reminder reminder : reminders) {

            if (reminder.isTriggered()) {
                continue;
            }

            if (reminder.getExtractedTime() == null) {
                continue;
            }

            try {
                LocalDateTime reminderTime = parseReminderTime(reminder.getExtractedTime());

                if (!reminderTime.isAfter(now)) {
                    System.out.println("Sending WhatsApp reminder: " + reminder.getExtractedTask());

                    twilioService.sendWhatsAppMessage(
                            reminder.getSender(),
                            "⏰ תזכורת: " + reminder.getExtractedTask()
                    );
                    reminder.setTriggered(true);
                }

            } catch (Exception e) {
                System.out.println("Failed to parse reminder time: " + reminder.getExtractedTime());
            }
        }
    }

    private LocalDateTime parseReminderTime(String timeText) {
        try {
            return LocalDateTime.parse(timeText);
        } catch (Exception e) {
            try {
                java.time.LocalTime localTime = java.time.LocalTime.parse(timeText);
                return java.time.LocalDate.now().atTime(localTime);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Could not parse time: " + timeText);
            }
        }
    }
}
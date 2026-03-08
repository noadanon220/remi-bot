package com.danono.remi_bot.model;

public class Reminder {

    private final String sender;
    private final String originalMessage;
    private final String extractedTask;

    public Reminder(String sender, String originalMessage, String extractedTask) {
        this.sender = sender;
        this.originalMessage = originalMessage;
        this.extractedTask = extractedTask;
    }

    public String getSender() {
        return sender;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public String getExtractedTask() {
        return extractedTask;
    }
}
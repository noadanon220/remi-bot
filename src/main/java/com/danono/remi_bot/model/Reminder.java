package com.danono.remi_bot.model;

public class Reminder {

    private final String sender;
    private final String originalMessage;
    private final String extractedTask;
    private final String extractedTime;
    private boolean triggered;

    public Reminder(String sender, String originalMessage, String extractedTask, String extractedTime) {
        this.sender = sender;
        this.originalMessage = originalMessage;
        this.extractedTask = extractedTask;
        this.extractedTime = extractedTime;
        this.triggered = false;
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

    public String getExtractedTime() {
        return extractedTime;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }
}
package com.danono.remi_bot.model;

public class IntentResult {

    private final MessageIntent intent;
    private final String extractedText;

    public IntentResult(MessageIntent intent, String extractedText) {
        this.intent = intent;
        this.extractedText = extractedText;
    }

    public MessageIntent getIntent() {
        return intent;
    }

    public String getExtractedText() {
        return extractedText;
    }
}
# Remi Bot 🤖

This is a small personal project I built while learning **Java + Spring Boot**.

The idea was to create a simple **WhatsApp bot** that can chat with you and also save reminders.

The bot receives messages from WhatsApp, sends them to an AI model, and responds back.
If the message looks like a reminder request, it saves it in memory.

---

## What the bot can do

Right now it can:

* Chat normally
* Detect if a message is a **reminder**
* Save reminders in memory
* Show saved reminders through an API endpoint

Example message:

```
תזכיר לי להתקשר לוטרינרית מחר ב-18:00
```

The bot detects that this is a reminder and stores it.

---

## Tech stack

* Java
* Spring Boot
* Spring AI
* OpenAI API
* Twilio WhatsApp Sandbox
* ngrok (for local testing)

---

## How it works (high level)

1. WhatsApp message is sent
2. Twilio sends a webhook to the Spring Boot server
3. The server processes the message
4. AI classifies the intent (chat / reminder / memory)
5. The bot responds back to WhatsApp

---

## Project structure

```
controller/
    WhatsAppController
    ReminderController

service/
    MessageService
    ReminderService

model/
    MessageIntent
    IntentResult
    Reminder
```

---

## Running the project

1. Clone the repository

```
git clone https://github.com/your-username/remi-bot.git
```

2. Create:

```
src/main/resources/application.properties
```

3. Add your OpenAI API key:

```
spring.ai.openai.api-key=YOUR_KEY
spring.ai.openai.chat.options.model=gpt-4o-mini
```

4. Run the Spring Boot application.

---

## Notes

This project is still very early and mostly for learning.




package com.danono.remi_bot.service;

import com.danono.remi_bot.model.Reminder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReminderService {

    private final List<Reminder> reminders = new ArrayList<>();

    public void saveReminder(Reminder reminder) {
        reminders.add(reminder);
        System.out.println("Reminder saved: " + reminder.getExtractedTask());
    }

    public List<Reminder> getAllReminders() {
        return reminders;
    }
}
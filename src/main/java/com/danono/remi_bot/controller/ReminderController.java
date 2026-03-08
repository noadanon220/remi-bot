package com.danono.remi_bot.controller;

import com.danono.remi_bot.model.Reminder;
import com.danono.remi_bot.service.ReminderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @GetMapping("/reminders")
    public List<Reminder> getReminders() {
        return reminderService.getAllReminders();
    }
}
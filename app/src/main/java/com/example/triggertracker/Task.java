package com.example.triggertracker;

import com.google.firebase.Timestamp;

public class Task {
    private String name;
    private Timestamp created;
    private Timestamp reminderTime;
    private boolean hasReminder;
    private String userId;

    public Task() {}

    public Task(String name, Timestamp created, Timestamp reminderTime, boolean hasReminder, String userId) {
        this.name = name;
        this.created = created;
        this.reminderTime = reminderTime;
        this.hasReminder = hasReminder;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedTimeStamp() {
        return created;
    }

    public Timestamp getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Timestamp reminderTime) {
        this.reminderTime = reminderTime;
    }

    public boolean getHasReminder() {
        return hasReminder;
    }

    public void setHasReminder(boolean hasReminder) {
        this.hasReminder = hasReminder;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", created=" + created +
                ", reminderTime=" + reminderTime +
                ", hasReminder=" + hasReminder +
                ", userId='" + userId + '\'' +
                '}';
    }
}

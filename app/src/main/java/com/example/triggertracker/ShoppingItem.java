package com.example.triggertracker;

import com.google.firebase.Timestamp;

public class ShoppingItem {
    private String name;
    private Timestamp created;
    private Timestamp reminderTime;
    private boolean hasReminder;
    private int qty;
    private String userId;

    public ShoppingItem() {
    }

    public ShoppingItem(String name, Timestamp created, Timestamp reminderTime, boolean hasReminder, int qty, String userId) {
        this.name = name;
        this.created = created;
        if(reminderTime == null) {
            this.reminderTime = created;
        } else {
            this.reminderTime = reminderTime;
        }
        this.hasReminder = hasReminder;
        this.qty = qty;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedTimestamp() {
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ShoppingItem{" +
                "name='" + name + '\'' +
                ", created=" + created +
                ", reminderTime=" + reminderTime +
                ", hasReminder=" + hasReminder +
                ", qty=" + qty +
                ", userId='" + userId + '\'' +
                '}';
    }
}

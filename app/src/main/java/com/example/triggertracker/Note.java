package com.example.triggertracker;

import android.util.Log;

import com.google.firebase.Timestamp;

public class Note {
    private String title;
    private String content;
    private String userId;
    private String documentId;
    private String TAG = "TAG";

    public Note() {

    }

    public Note(String title, String content, String userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public void setDocumentId(String documentId) {
        Log.d(TAG, "setDocumentId: " + documentId);
        this.documentId = documentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

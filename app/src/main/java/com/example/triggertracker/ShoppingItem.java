package com.example.triggertracker;

import android.net.Uri;

import com.google.firebase.Timestamp;
import com.google.firebase.storage.StorageReference;

public class ShoppingItem {
    private String name;
    private Timestamp created;
    private int qty;
    private String userId;
    private String documentId;
    private boolean isBought;
    private StorageReference imageReference;
    private Uri ShareUri;

    public Uri getShareUri() {
        return ShareUri;
    }

    public void setShareUri(Uri shareUri) {
        ShareUri = shareUri;
    }

    public StorageReference getImageReference() {
        return imageReference;
    }

    public void setImageReference(StorageReference reference) {
        this.imageReference = reference;
    }

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public ShoppingItem() {
    }

    public ShoppingItem(String name, Timestamp created, int qty, String userId) {
        this.name = name;
        this.created = created;
        this.qty = qty;
        this.userId = userId;
        this.isBought = false;
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
                ", qty=" + qty +
                ", userId='" + userId + '\'' +
                '}';
    }
}

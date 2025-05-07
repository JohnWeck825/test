package com.myshop.adminpage.controller.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notification {
    private String message;
    private String type; // "success", "danger", etc.
    private String category; // e.g., "Category", "Brand", etc.
    private LocalDateTime timestamp;

    public Notification(String message, String type, String category, LocalDateTime timestamp) {
        this.message = message;
        this.type = type;
        this.category = category;
        this.timestamp = timestamp;
    }
}

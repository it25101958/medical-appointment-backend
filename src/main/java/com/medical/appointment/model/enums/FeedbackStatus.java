package com.medical.appointment.model;

public enum FeedbackStatus {
    PENDING,    // submitted but not yet reviewed
    APPROVED,   // reviewed and visible publicly
    REJECTED,   // reviewed and hidden/removed
    ARCHIVED    // old feedback moved to archive
}
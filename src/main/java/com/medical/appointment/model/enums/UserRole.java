package com.medical.appointment.model.enums;

public enum UserRole {
    SUPER_ADMIN(1),
    ADMIN(2),
    STAFF(3),
    DOCTOR(4),
    PATIENT(5);

    private final int value;

    UserRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // Optional: Useful helper method for later
    public static UserRole fromInt(int value) {
        for (UserRole role : UserRole.values()) {
            if (role.value == value) {
                return role;
            }
        }
        return PATIENT;
    }
}
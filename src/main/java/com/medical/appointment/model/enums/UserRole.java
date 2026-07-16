package com.medical.appointment.model.enums;

public enum UserRole {
    ADMIN(1),
    STAFF(2),
    DOCTOR(3),
    PATIENT(4);

    private final int value;

    UserRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UserRole fromInt(int value) {
        for (UserRole role : UserRole.values()) {
            if (role.value == value) {
                return role;
            }
        }
        return PATIENT;
    }
}
package com.medical.appointment.util;

public final class PasswordPolicy {

    private PasswordPolicy() {
    }

    public static final String STRONG_PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._#^-])[A-Za-z\\d@$!%*?&._#^-]{8,72}$";

    public static final String STRONG_PASSWORD_MESSAGE =
            "Password must be 8-72 characters and include uppercase, lowercase, number, and special character";
}
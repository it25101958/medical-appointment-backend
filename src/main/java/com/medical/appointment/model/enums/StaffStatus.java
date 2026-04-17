package com.medical.appointment.model.enums;

public enum StaffStatus {
    ACTIVE("Active"),
    ON_LEAVE("On Leave"),
    INACTIVE("Inactive"),
    TERMINATED("Terminated");

    private final String label;

    StaffStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static StaffStatus fromLabel(String label) {
        for (StaffStatus status : StaffStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        return INACTIVE;
    }
}
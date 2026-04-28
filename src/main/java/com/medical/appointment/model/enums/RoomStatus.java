package com.medical.appointment.model.enums;

public enum RoomStatus {
    AVAILABLE,
    OCCUPIED,
    MAINTENANCE;


    public String getValue() {
        return this.name();
    }
}

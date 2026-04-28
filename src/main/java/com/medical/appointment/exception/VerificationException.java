package com.medical.appointment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public class VerificationException extends RuntimeException {
    public VerificationException(String message) {
        super(message);
    }
}
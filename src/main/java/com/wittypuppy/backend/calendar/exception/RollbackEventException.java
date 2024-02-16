package com.wittypuppy.backend.calendar.exception;

public class RollbackEventException extends RuntimeException {
    public RollbackEventException(String message) {
        super(message);
    }
}

package com.wittypuppy.backend.calendar.exception;

public class DeleteEventException extends RuntimeException {
    public DeleteEventException(String message) {
        super(message);
    }
}

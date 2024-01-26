package com.wittypuppy.backend.project.exception;

public class ProjectIsLockedException extends RuntimeException {
    public ProjectIsLockedException(String message) {
        super(message);
    }
}

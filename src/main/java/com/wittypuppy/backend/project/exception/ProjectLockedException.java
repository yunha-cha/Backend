package com.wittypuppy.backend.project.exception;

public class ProjectLockedException extends RuntimeException {
    public ProjectLockedException(String message) {
        super(message);
    }
}

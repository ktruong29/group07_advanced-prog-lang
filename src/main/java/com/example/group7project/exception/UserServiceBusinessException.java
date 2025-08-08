package com.example.group7project.exception;

public class UserServiceBusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserServiceBusinessException(String message) {
        super(message);
    }

    public UserServiceBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

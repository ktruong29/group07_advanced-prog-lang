package com.example.group7project.exception;

public class CustomerServiceBusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CustomerServiceBusinessException(String message) {
        super(message);
    }

    public CustomerServiceBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.example.group7project.exception;

public class OrderServiceBusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public OrderServiceBusinessException(String message) {
        super(message);
    }

    public OrderServiceBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

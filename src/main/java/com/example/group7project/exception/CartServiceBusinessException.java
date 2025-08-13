package com.example.group7project.exception;

public class CartServiceBusinessException extends RuntimeException {
    public CartServiceBusinessException(String message) {
        super(message);
    }
}

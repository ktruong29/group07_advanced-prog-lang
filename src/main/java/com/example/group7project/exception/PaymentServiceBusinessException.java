package com.example.group7project.exception;

public class PaymentServiceBusinessException extends RuntimeException {
    public PaymentServiceBusinessException(String message) {
        super(message);
    }
}

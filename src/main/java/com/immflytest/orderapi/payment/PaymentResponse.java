package com.immflytest.orderapi.payment;

public class PaymentResponse {
    private boolean success;
    private String message;

    public PaymentResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and Setters
}
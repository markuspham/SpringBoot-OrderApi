package com.immflytest.orderapi.payment;

import org.springframework.stereotype.Service;

@Service
public class MockPaymentService {

    public PaymentResponse processPayment(PaymentRequest request) {
        
        // Fake logic: success if token starts with "valid"
        if (request.getCardToken().startsWith("valid")) {
            return new PaymentResponse(true, "Payment successful");
        } else {
            return new PaymentResponse(false, "Payment failed: invalid token");
        }
    }
}

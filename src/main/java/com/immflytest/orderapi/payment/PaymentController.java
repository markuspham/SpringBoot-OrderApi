package com.immflytest.orderapi.payment;

// import com.immflytest.orderapi.payment.MockPaymentService;
// import com.immflytest.orderapi.payment.PaymentRequest;
// import com.immflytest.orderapi.payment.PaymentResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final MockPaymentService paymentService;

    public PaymentController(MockPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public PaymentResponse processPayment(@RequestBody PaymentRequest request) {
        return paymentService.processPayment(request);
    }
}
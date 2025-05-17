package com.immflytest.orderapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.immflytest.orderapi.model.Order;
import com.immflytest.orderapi.model.PaymentStatus;
import com.immflytest.orderapi.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public Order createOrder(@RequestParam String seatLetter,
                              @RequestParam String seatNumber) {
        return orderService.createEmptyOrder(seatLetter, seatNumber);
    }

    @PutMapping("/{orderId}/add-product")
    public Order addProduct(@PathVariable Long orderId,
                            @RequestParam Long productId,
                            @RequestParam int quantity,
                            @RequestParam String buyerEmail) {
        return orderService.addProductToOrder(orderId, productId, quantity, buyerEmail);
    }

    @PostMapping("/{orderId}/finish")
    public ResponseEntity<Order> finishOrder(@PathVariable Long orderId,
                             @RequestParam PaymentStatus paymentStatus,
                             @RequestParam String cardToken,
                             @RequestParam String paymentGateway) {
        Order order = orderService.finishOrder(orderId, paymentStatus, cardToken, paymentGateway);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/cancel")
    public Order cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }
}
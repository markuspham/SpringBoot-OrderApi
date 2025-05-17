package com.immflytest.orderapi.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.immflytest.orderapi.model.Order;
import com.immflytest.orderapi.model.OrderProduct;
import com.immflytest.orderapi.model.OrderStatus;
import com.immflytest.orderapi.model.PaymentStatus;
import com.immflytest.orderapi.model.Product;
import com.immflytest.orderapi.repository.OrderProductRepository;
import com.immflytest.orderapi.repository.OrderRepository;
import com.immflytest.orderapi.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
    }

    public Order createEmptyOrder(String seatLetter, String seatNumber) {
        Order order = new Order();
        order.setSeatLetter(seatLetter);
        order.setSeatNumber(seatNumber);
        order.setStatus(OrderStatus.OPEN);
        return orderRepository.save(order);
    }

    @Transactional
    public Order addProductToOrder(Long orderId, Long productId, int quantity, String buyerEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        // Update stock
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        // Add to order
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrder(order);
        orderProduct.setProduct(product);
        orderProduct.setQuantity(quantity);
        orderProductRepository.save(orderProduct);

        order.getOrderProducts().add(orderProduct);
        order.setBuyerEmail(buyerEmail);
        recalculateTotalPrice(order);

        return orderRepository.save(order);
    }

    public void recalculateTotalPrice(Order order) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderProduct op : order.getOrderProducts()) {
            total = total.add(op.getProduct().getPrice().multiply(BigDecimal.valueOf(op.getQuantity())));
        }
        order.setTotalPrice(total);
    }

    public Order finishOrder(Long orderId, PaymentStatus paymentStatus, String cardToken, String gateway) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.FINISHED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order cannot be finished");
        }

        order.setPaymentStatus(paymentStatus);
        order.setPaymentDate(LocalDateTime.now());
        order.setCardToken(cardToken);
        order.setPaymentGateway(gateway);
        order.setStatus(OrderStatus.FINISHED);

        return orderRepository.save(order);
    }

    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
}
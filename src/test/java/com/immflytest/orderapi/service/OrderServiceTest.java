package com.immflytest.orderapi.service;

import com.immflytest.orderapi.model.*;
import com.immflytest.orderapi.repository.OrderProductRepository;
import com.immflytest.orderapi.repository.OrderRepository;
import com.immflytest.orderapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private OrderProductRepository orderProductRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        productRepository = mock(ProductRepository.class);
        orderProductRepository = mock(OrderProductRepository.class);
        orderService = new OrderService(orderRepository, productRepository, orderProductRepository);
    }

    @Test
    void testAddProductToOrder_success() {
        // Arrange
        Long orderId = 1L;
        Long productId = 10L;
        int quantity = 2;

        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("9.99"));
        product.setStock(10);

        Order order = new Order();
        order.setId(orderId);
        order.setSeatLetter("A");
        order.setSeatNumber("12");
        order.setStatus(OrderStatus.OPEN);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order updatedOrder = orderService.addProductToOrder(orderId, productId, quantity, "test@example.com");

        // Assert
        assertNotNull(updatedOrder);
        assertEquals("test@example.com", updatedOrder.getBuyerEmail());
        assertEquals(1, updatedOrder.getOrderProducts().size());
        assertEquals(new BigDecimal("19.98"), updatedOrder.getTotalPrice());
    }

    @Test
    void testAddProductToOrder_insufficientStock() {
        Long orderId = 2L;
        Long productId = 99L;
        int quantity = 5;

        Product product = new Product();
        product.setId(productId);
        product.setStock(2); // not enough

        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                orderService.addProductToOrder(orderId, productId, quantity, "test@example.com"));

        assertTrue(exception.getMessage().contains("Not enough stock"));
    }

    @Test
    void testFinishOrder_success() {
        Order order = new Order();
        order.setStatus(OrderStatus.OPEN);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.finishOrder(1L, PaymentStatus.PAID, "token123", "TestGateway");

        assertEquals(OrderStatus.FINISHED, result.getStatus());
        assertEquals(PaymentStatus.PAID, result.getPaymentStatus());
        assertEquals("token123", result.getCardToken());
        assertEquals("TestGateway", result.getPaymentGateway());
        assertNotNull(result.getPaymentDate());
    }
}
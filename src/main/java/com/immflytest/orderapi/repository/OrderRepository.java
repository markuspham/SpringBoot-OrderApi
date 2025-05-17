package com.immflytest.orderapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.immflytest.orderapi.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}

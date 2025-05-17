package com.immflytest.orderapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.immflytest.orderapi.model.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    
}

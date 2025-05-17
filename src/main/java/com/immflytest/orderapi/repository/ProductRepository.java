package com.immflytest.orderapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.immflytest.orderapi.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}

package com.immflytest.orderapi.repository;

import com.immflytest.orderapi.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    
}

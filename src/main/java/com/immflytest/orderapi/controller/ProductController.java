package com.immflytest.orderapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.immflytest.orderapi.model.Product;
import com.immflytest.orderapi.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }
}
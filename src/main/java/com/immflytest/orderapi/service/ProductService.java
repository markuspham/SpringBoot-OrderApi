package com.immflytest.orderapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.immflytest.orderapi.model.Product;
import com.immflytest.orderapi.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateStock(Long productId, int newStock) {
        Product product = getProductById(productId);
        product.setStock(newStock);
        return productRepository.save(product);
    }
}
package com.tiago.ecommerce.product;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    private final String PRODUCT_NOT_FOUND = "Product not found";

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public ResponseEntity<Object> delete(UUID id) {
        Product product = productRepository.findById(id).orElse(null);

        if(product == null) {
            return ResponseEntity.status(404).body(PRODUCT_NOT_FOUND);
        }

        productRepository.delete(product);
        return ResponseEntity.ok("Product deleted successfully");
    }

    public ResponseEntity<Object> getById(UUID id) {
        Product product = productRepository.findById(id).orElse(null);

        if(product == null) {
            return ResponseEntity.status(404).body(PRODUCT_NOT_FOUND);
        }

        return ResponseEntity.ok(product);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Transactional
    public ResponseEntity<Object> update(UUID id, ProductDto updatedProductDto) {
        Product product = productRepository.findById(id).orElse(null);

        if(product == null) {
            return ResponseEntity.status(404).body(PRODUCT_NOT_FOUND);
        }

        BeanUtils.copyProperties(updatedProductDto, product);

        return ResponseEntity.ok(productRepository.save(product));
    }

    public List<Product> getAllByCategory(String categoryName) {
        return productRepository.findByCategories_NameIgnoreCase(categoryName);
    }
}
package com.tiago.ecommerce.product;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public Iterable<Product> getProducts() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable UUID id) {
        return productService.getById(id);
    }

    @GetMapping("/category/{categoryName}")
    public Iterable<Product> getProductsByCategory(@PathVariable String categoryName) {
        return productService.getAllByCategory(categoryName);
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public Product createProduct(@RequestBody ProductDto productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        return productService.save(product);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> updateProduct(@PathVariable UUID id, @RequestBody ProductDto productDto) {
        return productService.update(id, productDto);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> deleteProduct(@PathVariable UUID id) {
        return productService.delete(id);
    }
}
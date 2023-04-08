package com.tiago.ecommerce.category;

import com.tiago.ecommerce.product.Product;
import com.tiago.ecommerce.product.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private final String CATEGORY_NOT_FOUND = "Category not found";

    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public ResponseEntity<Object> delete(UUID id) {
        Category category = categoryRepository.findById(id).orElse(null);

        if(category == null) {
            return ResponseEntity.status(404).body(CATEGORY_NOT_FOUND);
        }

        categoryRepository.delete(category);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Object> getById(UUID id) {
        Category category = categoryRepository.findById(id).orElse(null);

        if(category == null) {
            return ResponseEntity.status(404).body(CATEGORY_NOT_FOUND);
        }

        return ResponseEntity.ok(category);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public ResponseEntity<Object> update(UUID id, CategoryDto updatedCategoryDto) {
        Category category = categoryRepository.findById(id).orElse(null);

        if(category == null) {
            return ResponseEntity.status(404).body(CATEGORY_NOT_FOUND);
        }

        BeanUtils.copyProperties(updatedCategoryDto, category);

        return ResponseEntity.ok(categoryRepository.save(category));
    }

    public ResponseEntity<Object> addProductToCategory(UUID categoryId, UUID productId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);

        if(category == null) {
            return ResponseEntity.status(404).body(CATEGORY_NOT_FOUND);
        }

        if(product == null) {
            return ResponseEntity.status(404).body("Product not found");
        }

        category.getProducts().add(product);
        product.getCategories().add(category);

        categoryRepository.save(category);
        productRepository.save(product);

        return ResponseEntity.ok("Product added to category");
    }
}

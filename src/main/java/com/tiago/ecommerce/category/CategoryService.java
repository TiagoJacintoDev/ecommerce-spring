package com.tiago.ecommerce.category;

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

    private final String CATEGORY_NOT_FOUND = "Category not found";

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

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

    public ResponseEntity<Object> update(UUID id, CategoryDto updatedCategoryDto) {
        Category category = categoryRepository.findById(id).orElse(null);

        if(category == null) {
            return ResponseEntity.status(404).body(CATEGORY_NOT_FOUND);
        }

        BeanUtils.copyProperties(updatedCategoryDto, category);

        return ResponseEntity.ok(categoryRepository.save(category));
    }
}

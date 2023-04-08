package com.tiago.ecommerce.category;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/categorys")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Iterable<Category> getCategorys() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable UUID id) {
        return categoryService.getById(id);
    }

    @PostMapping
    public Category createCategory(@RequestBody CategoryDto categoryDto) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);
        return categoryService.save(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable UUID id, @RequestBody CategoryDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable UUID id) {
        return categoryService.delete(id);
    }

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<Object> addProductToCategory(@PathVariable UUID categoryId, @PathVariable UUID productId) {
        return categoryService.addProductToCategory(categoryId, productId);
    }
}

package com.example.springbootbackend.category.controller;


import com.example.springbootbackend.category.dto.CategoryDTO;
import com.example.springbootbackend.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/category/")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDTO);
    }

    @GetMapping("vocabulary/{vocabularyId}")
    public ResponseEntity<?> getCategoriesByVocabularyId(@PathVariable Long vocabularyId) {
        return ResponseEntity.ok(categoryService.getCategoriesByVocabularyId(vocabularyId));
    }

    @PatchMapping("{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryDTO);
        return ResponseEntity.ok(updatedCategory);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(createdCategory);
    }


    @PostMapping("sampleData")
    public ResponseEntity<?> createSampleData() {
        categoryService.createSampleData();
        return ResponseEntity.ok("Sample data created successfully");
    }


}

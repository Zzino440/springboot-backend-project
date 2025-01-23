package com.example.springbootbackend.category.controller;


import com.example.springbootbackend.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/category/")
public class CategoryController {


    private final CategoryService categoryService;


    @PostMapping("sampleData")
    public ResponseEntity<?> createSampleData() {
        categoryService.createSampleData();
        return ResponseEntity.ok("Sample data created successfully");
    }


}

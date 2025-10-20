package com.example.springbootbackend.category.mapper;

import com.example.springbootbackend.category.dto.CategoryDTO;
import com.example.springbootbackend.category.model.Category;

import java.util.stream.Collectors;

public class CategoryMapper {

    public static CategoryDTO toDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .vocabularyId(category.getVocabulary() != null ? category.getVocabulary().getId() : null)
                .parentCategoryId(category.getParentCategory() != null ? category.getParentCategory().getId() : null)
                .parentCategoryName(category.getParentCategory() != null ? category.getParentCategory().getName() : null)
                .build();
    }
}

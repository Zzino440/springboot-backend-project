package com.example.springbootbackend.category.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Long vocabularyId;
    private Long parentCategoryId;
    private String parentCategoryName;
}

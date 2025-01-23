package com.example.springbootbackend.category.repository;

import com.example.springbootbackend.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByVocabularyId(Long vocabularyId);

    List<Category> findByParentCategoryId(Long parentCategoryId);
}

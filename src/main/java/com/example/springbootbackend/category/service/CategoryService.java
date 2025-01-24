package com.example.springbootbackend.category.service;

import com.example.springbootbackend.category.dto.CategoryDTO;
import com.example.springbootbackend.category.mapper.CategoryMapper;
import com.example.springbootbackend.category.model.Category;
import com.example.springbootbackend.category.repository.CategoryRepository;
import com.example.springbootbackend.vocabulary.model.Vocabulary;
import com.example.springbootbackend.vocabulary.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {


    private final VocabularyRepository vocabularyRepository;
    private final CategoryRepository categoryRepository;


    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return CategoryMapper.toDTO(category);
    }

    public List<CategoryDTO> getCategoriesByVocabularyId(Long vocabularyId) {
        List<Category> categories = categoryRepository.findByVocabularyId(vocabularyId);
        return categories.stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void createSampleData() {
        // Creazione Vocabulary
        Vocabulary legalEntity = new Vocabulary();
        legalEntity.setName("Legal Entity");
        legalEntity.setVocabularyCode("LE");
        legalEntity.setDescription("Legal entities like companies or organizations");
        vocabularyRepository.save(legalEntity);

        Vocabulary businessUnit = new Vocabulary();
        businessUnit.setName("Business Unit");
        businessUnit.setVocabularyCode("BU");
        businessUnit.setDescription("Business Units within Legal Entities");
        vocabularyRepository.save(businessUnit);

        // Creazione Category per Legal Entity
        Category appo = new Category();
        appo.setName("APPO");
        appo.setDescription("APPO description");
        appo.setVocabulary(legalEntity);
        categoryRepository.save(appo);

        Category ebdin = new Category();
        ebdin.setName("EBDIN");
        ebdin.setDescription("EBDIN description");
        ebdin.setVocabulary(legalEntity);
        categoryRepository.save(ebdin);

        // Creazione Category per Business Unit
        Category tech = new Category();
        tech.setName("Tech");
        tech.setDescription("Technology Business Unit");
        tech.setVocabulary(businessUnit);
        tech.setParentCategory(appo); // Associata a APPO
        categoryRepository.save(tech);

        Category marketing = new Category();
        marketing.setName("Marketing");
        marketing.setDescription("Marketing Business Unit");
        marketing.setVocabulary(businessUnit);
        marketing.setParentCategory(appo); // Associata a APPO
        categoryRepository.save(marketing);
    }
}

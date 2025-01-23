package com.example.springbootbackend.category.service;

import com.example.springbootbackend.category.model.Category;
import com.example.springbootbackend.category.repository.CategoryRepository;
import com.example.springbootbackend.vocabulary.model.Vocabulary;
import com.example.springbootbackend.vocabulary.repository.VocabularyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private VocabularyRepository vocabularyRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public void createSampleData() {
        // Creazione Vocabulary
        Vocabulary legalEntity = new Vocabulary();
        legalEntity.setName("Legal Entity");
        legalEntity.setDescription("Legal entities like companies or organizations");
        vocabularyRepository.save(legalEntity);

        Vocabulary businessUnit = new Vocabulary();
        businessUnit.setName("Business Unit");
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

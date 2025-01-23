package com.example.springbootbackend.vocabulary.service;

import com.example.springbootbackend.vocabulary.DTO.VocabularyDto;
import com.example.springbootbackend.vocabulary.mapper.VocabularyMapper;
import com.example.springbootbackend.vocabulary.model.Vocabulary;
import com.example.springbootbackend.vocabulary.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VocabularyService {

    private final VocabularyRepository vocabularyRepository;

    public List<VocabularyDto> getAllVocabularies() {
        List<Vocabulary> vocabularyList = vocabularyRepository.findAll();
        return VocabularyMapper.toDtoList(vocabularyList);
    }
}

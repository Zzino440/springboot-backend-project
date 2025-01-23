package com.example.springbootbackend.vocabulary.mapper;

import com.example.springbootbackend.vocabulary.DTO.VocabularyDto;
import com.example.springbootbackend.vocabulary.model.Vocabulary;

import java.util.List;
import java.util.stream.Collectors;

public class VocabularyMapper {

    public static VocabularyDto toDto(Vocabulary vocabulary) {
        return VocabularyDto.builder()
                .id(vocabulary.getId())
                .vocabularyCode(vocabulary.getVocabularyCode())
                .description(vocabulary.getDescription())
                .name(vocabulary.getName())
                .build();
    }

    public static Vocabulary toEntity(VocabularyDto vocabularyDto) {
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setId(vocabularyDto.getId());
        vocabulary.setVocabularyCode(vocabularyDto.getVocabularyCode());
        vocabulary.setDescription(vocabularyDto.getDescription());
        vocabulary.setName(vocabularyDto.getName());
        return vocabulary;
    }

    public static List<VocabularyDto> toDtoList(List<Vocabulary> vocabularies) {
        return vocabularies.stream()
                .map(VocabularyMapper::toDto)
                .collect(Collectors.toList());
    }

}

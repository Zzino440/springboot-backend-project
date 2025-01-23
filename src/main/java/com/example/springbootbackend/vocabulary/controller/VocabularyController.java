package com.example.springbootbackend.vocabulary.controller;

import com.example.springbootbackend.vocabulary.DTO.VocabularyDto;
import com.example.springbootbackend.vocabulary.service.VocabularyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/vocabulary/")
@RequiredArgsConstructor
public class VocabularyController {

    private final VocabularyService vocabularyService;

    @GetMapping("getAll")
    public ResponseEntity<List<VocabularyDto>> getAllVocabularies() {
        return ResponseEntity.ok(vocabularyService.getAllVocabularies());
    }
}

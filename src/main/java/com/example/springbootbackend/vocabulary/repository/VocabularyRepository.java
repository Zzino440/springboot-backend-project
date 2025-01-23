package com.example.springbootbackend.vocabulary.repository;

import com.example.springbootbackend.vocabulary.model.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {


}

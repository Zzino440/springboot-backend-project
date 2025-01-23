package com.example.springbootbackend.vocabulary.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "vocabulary_code", nullable = false)
    private String vocabularyCode;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

}

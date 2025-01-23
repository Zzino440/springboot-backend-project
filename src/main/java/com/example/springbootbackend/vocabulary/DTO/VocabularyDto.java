package com.example.springbootbackend.vocabulary.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.springbootbackend.vocabulary.model.Vocabulary}
 */
@Value
@Builder
@AllArgsConstructor
public class VocabularyDto implements Serializable {
    @NotNull
    Long id;
    @NotNull
    String vocabularyCode;
    String description;
    String name;
}

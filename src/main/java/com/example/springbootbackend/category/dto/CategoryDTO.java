package com.example.springbootbackend.category.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CategoryDTO {

    private Long id;                      // ID della categoria
    private String name;                  // Nome della categoria
    private String description;           // Descrizione della categoria
/*    private Long vocabularyId;            // ID del vocabolario associato
    private String vocabularyName;   */     // Nome del vocabolario associato
    private Long parentCategoryId;        // ID della categoria genitore (se esiste)
    private String parentCategoryName;    // Nome della categoria genitore (se esiste)
/*    private List<SubCategoryDTO> subCategories; // Lista di sottocategorie

    @Data
    @Builder
    public static class SubCategoryDTO {
        private Long id;                  // ID della sottocategoria
        private String name;              // Nome della sottocategoria
    }*/
}

package com.DebboCollect.DebboCollect.Model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectRequest {
    @NotNull(message = "La date de collecte est obligatoire")
    private LocalDate dateCollecte;
    @NotNull(message = "La localisation est obligatoire")
    private String localisation;
    @NotNull(message = "L enqueteur est obligatoire")
    private Long enqueteurId;
    @NotNull(message = "Le projet est obligatoire")
    private Long projetId;
}
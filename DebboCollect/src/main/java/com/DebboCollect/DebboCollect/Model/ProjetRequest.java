package com.DebboCollect.DebboCollect.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjetRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    @NotBlank(message = "La description est obligatoire")
    private String description;
    @NotBlank(message = "La zone géographique est obligatoire")
    private String zoneGeographique;
    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;
    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;
    @NotNull(message = "le type est obligatoire")
    private String type;
}

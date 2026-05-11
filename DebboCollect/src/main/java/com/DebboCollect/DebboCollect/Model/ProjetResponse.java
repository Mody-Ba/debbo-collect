package com.DebboCollect.DebboCollect.Model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjetResponse {

    private Long id;

    private String nom;

    private String description;

    private String zoneGeographique;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private String type;
}

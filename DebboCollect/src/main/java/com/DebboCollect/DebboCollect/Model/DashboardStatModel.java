package com.DebboCollect.DebboCollect.Model;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatModel {
    // Collectes
    private Integer totalCollectes;
    private Integer collectesValidees;
    private Integer collectesEnAttente;
    private Integer collectesRevision;
    private Integer collectesEnvoyees;
    private Integer collectesEnregistrees;

    // Projet
    private Integer nombreEnqueteurs;
    private Integer nombreQuestions;

    // Statistiques dynamiques
    private Integer nombreHommes;
    private Integer nombreFemmes;

    private Integer nombreEnfants;
    private Integer nombreAdultes;
    private Integer nombrePersonnesAgees;


    private Double pourcentageValidation;

    private Double pourcentageEnAttente;

    private Double pourcentageRevision;

    private Double pourcentageEnvoyees;

    private Double pourcentageEnregistrees;

    private List<DashboardCardModel> cartesDynamiques;

}


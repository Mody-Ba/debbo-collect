package com.DebboCollect.DebboCollect.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    private Long nombreProjets;

    private Long nombreCollectes;

    private Long nombreReponses;

    private Long nombreUtilisateurs;

    private long collectesValidees;
    private long collectesEnAttente;

    private Double pourcentageValidation;

    private Double pourcentageCollectes;



}

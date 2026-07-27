package com.DebboCollect.DebboCollect.Model;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
public class LotCollectResponse {
    private Long id;

    private String nomEnqueteur;

    private Long projetId;

    private String nomProjet;


    private Long enqueteurId;

    private String statut;

    private LocalDate dateEnvoi;

    private Integer nombreCollectes;

    private List<CollectResponse> collectes;
}

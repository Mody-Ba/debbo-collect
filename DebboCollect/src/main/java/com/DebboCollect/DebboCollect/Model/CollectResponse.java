package com.DebboCollect.DebboCollect.Model;

import com.DebboCollect.DebboCollect.entity.StatusCollect;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectResponse {

    private Long id;

    private LocalDate dateCollecte;

    private String localisation;

    private Double latitude;

    private Double longitude;

    private Long enqueteurId;

    private Long projetId;
    private StatusCollect statut;
}
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

    private Long enqueteurId;

    private Integer numeroCollecteProjet;

    private Long projetId;

    private String nomProjet;

    private StatusCollect statut;


}
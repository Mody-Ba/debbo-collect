package com.DebboCollect.DebboCollect.Model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectModel {

    private Long id;

    private LocalDate dateCollecte;

    private String localisation;

    private Long enqueteurId;

    private Long projetId;
}
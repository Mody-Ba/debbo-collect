package com.DebboCollect.DebboCollect.mappers;

import com.DebboCollect.DebboCollect.Model.LotCollectResponse;
import com.DebboCollect.DebboCollect.entity.LotCollect;
import com.DebboCollect.DebboCollect.entity.Projet;
import com.DebboCollect.DebboCollect.entity.StatusCollect;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LotCollectMapper {

    private final CollectMapper collectMapper;

    public LotCollectResponse toResponse(
            LotCollect lot,
            Integer nombreCollectes
    ) {

        return LotCollectResponse.builder()
                .id(lot.getId())
                .projetId(lot.getProjet().getId())
                .nomProjet(lot.getProjet().getNom())
                .enqueteurId(lot.getEnqueteur().getId())
                .nomEnqueteur(lot.getEnqueteur().getNom())
                .statut(lot.getStatut().name())
                .dateEnvoi(lot.getDateEnvoi())
                .nombreCollectes(nombreCollectes)

                .collectes(
                        lot.getCollectes() != null
                                ? lot.getCollectes()
                                  .stream()
                                  .map(collectMapper::toResponse)
                                  .collect(Collectors.toList())
                                : null
                )

                .build();
    }

    public LotCollect toEntity(
            Projet projet,
            Utilisateur enqueteur
    ) {

        return LotCollect.builder()
                .dateEnvoi(LocalDate.now())
                .projet(projet)
                .enqueteur(enqueteur)
                .statut(StatusCollect.EN_ATTENTE)
                .build();
    }
}
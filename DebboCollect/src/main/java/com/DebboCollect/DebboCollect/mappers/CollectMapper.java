package com.DebboCollect.DebboCollect.mappers;

import com.DebboCollect.DebboCollect.Model.CollectModel;
import com.DebboCollect.DebboCollect.Model.CollectRequest;
import com.DebboCollect.DebboCollect.Model.CollectResponse;
import com.DebboCollect.DebboCollect.entity.Collecte;
import com.DebboCollect.DebboCollect.entity.Projet;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import org.springframework.stereotype.Component;

@Component
public class CollectMapper {
    public Collecte toEntity(CollectRequest request,
                             Utilisateur enqueteur,
                             Projet projet) {

        return Collecte.builder()
                .dateCollecte(request.getDateCollecte())
                .localisation(request.getLocalisation())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .enqueteur(enqueteur)
                .projet(projet)
                .build();
    }

    public CollectResponse toResponse(Collecte collecte) {

        return CollectResponse.builder()
                .id(collecte.getId())
                .dateCollecte(collecte.getDateCollecte())
                .localisation(collecte.getLocalisation())
                .latitude(collecte.getLatitude())
                .longitude(collecte.getLongitude())
                .enqueteurId(collecte.getEnqueteur().getId())
                .projetId(collecte.getProjet().getId())
                .build();
    }

    public CollectModel toModel(Collecte collecte) {

        return CollectModel.builder()
                .id(collecte.getId())
                .dateCollecte(collecte.getDateCollecte())
                .localisation(collecte.getLocalisation())
                .latitude(collecte.getLatitude())
                .longitude(collecte.getLongitude())
                .enqueteurId(collecte.getEnqueteur().getId())
                .projetId(collecte.getProjet().getId())
                .build();
    }
}

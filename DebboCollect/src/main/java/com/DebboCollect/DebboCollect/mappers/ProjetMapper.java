package com.DebboCollect.DebboCollect.mappers;

import com.DebboCollect.DebboCollect.Model.ProjetModel;
import com.DebboCollect.DebboCollect.Model.ProjetRequest;
import com.DebboCollect.DebboCollect.Model.ProjetResponse;
import com.DebboCollect.DebboCollect.entity.Projet;
import org.springframework.stereotype.Component;

@Component
public class ProjetMapper {

    public Projet toEntity(ProjetRequest request) {

        return Projet.builder()
                .nom(request.getNom())
                .description(request.getDescription())
                .zoneGeographique(request.getZoneGeographique())
                .dateDebut(request.getDateDebut())
                .dateFin(request.getDateFin())
                .type(request.getType())
                .build();
    }

    public ProjetResponse toResponse(Projet projet) {

        return ProjetResponse.builder()
                .id(projet.getId())
                .nom(projet.getNom())
                .description(projet.getDescription())
                .zoneGeographique(projet.getZoneGeographique())
                .dateDebut(projet.getDateDebut())
                .dateFin(projet.getDateFin())
                .type(projet.getType())
                .build();
    }

    public ProjetModel toModel(Projet projet) {

        return ProjetModel.builder()
                .id(projet.getId())
                .nom(projet.getNom())
                .description(projet.getDescription())
                .zoneGeographique(projet.getZoneGeographique())
                .dateDebut(projet.getDateDebut())
                .dateFin(projet.getDateFin())
                .type(projet.getType())
                .build();
    }

    public void updateEntityFromRequest(ProjetRequest request, Projet projet) {

        projet.setNom(request.getNom());
        projet.setDescription(request.getDescription());
        projet.setZoneGeographique(request.getZoneGeographique());
        projet.setDateDebut(request.getDateDebut());
        projet.setDateFin(request.getDateFin());
        projet.setType(request.getType());
    }
}

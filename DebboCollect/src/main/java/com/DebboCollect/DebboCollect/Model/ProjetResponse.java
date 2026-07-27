package com.DebboCollect.DebboCollect.Model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.DebboCollect.DebboCollect.entity.StatutProjet;

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

    private LocalDateTime dateEnvoiBailleur;

    private String type;

    private StatutProjet statut;

    private String nomSuperviseur;

    private UtilisateurResponse bailleur;

    private List<UtilisateurResponse> enqueteurs;
}

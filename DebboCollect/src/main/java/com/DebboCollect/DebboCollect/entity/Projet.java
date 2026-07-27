package com.DebboCollect.DebboCollect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String nom;

    private String description;

    private String zoneGeographique;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private LocalDateTime dateEnvoiBailleur;

    private String type;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatutProjet statut = StatutProjet.EN_COURS;

    @ManyToOne
    private Utilisateur superviseur;

    @ManyToOne
    private Utilisateur bailleur;

    @OneToMany(mappedBy = "projet")
    private List<Utilisateur> enqueteurs;

    @OneToMany(mappedBy = "projet")
    private List<Champ> champs;

    @OneToMany(mappedBy = "projet")
    private List<Collecte> collectes;
}

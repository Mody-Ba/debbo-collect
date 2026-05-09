package com.DebboCollect.DebboCollect.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String nom;

    private String description;

    private String zoneGeographique;

    private Date dateDebut;

    private Date dateFin;

    @ManyToOne
    private Utilisateur superviseur;

    @ManyToOne
    private Utilisateur bailleur;

    @OneToMany(mappedBy = "projet")
    private List<Champ> champs;

    @OneToMany(mappedBy = "projet")
    private List<Collecte> collectes;
}

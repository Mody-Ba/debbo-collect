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

public class Collecte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    private String statut;

    private double latitude;

    private double longitude;

    @ManyToOne
    private Projet projet;

    @ManyToOne
    private Utilisateur enqueteur;

    @OneToMany(mappedBy = "collecte")
    private List<Reponse> reponses;
}

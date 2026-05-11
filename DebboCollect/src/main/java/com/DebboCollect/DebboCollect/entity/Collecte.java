package com.DebboCollect.DebboCollect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Collecte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateCollecte;

    private String localisation;

    @ManyToOne
    private Utilisateur enqueteur;

    @ManyToOne
    private Projet projet;

    @OneToMany(mappedBy = "collecte")
    private List<Reponse> reponses;
}
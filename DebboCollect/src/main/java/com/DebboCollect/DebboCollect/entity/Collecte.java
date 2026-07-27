package com.DebboCollect.DebboCollect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private Integer numeroCollecteProjet;

    private LocalDateTime dateValidation;

    @ManyToOne
    private Utilisateur enqueteur;

    @ManyToOne
    private Projet projet;

    @ManyToOne
    private LotCollect lotCollecte;

    @OneToMany(mappedBy = "collecte")
    private List<Reponse> reponses;

    @Enumerated(EnumType.STRING)
    private StatusCollect statut;
}
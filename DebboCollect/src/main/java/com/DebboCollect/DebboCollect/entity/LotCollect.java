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
@Table(name = "lot_collecte")
public class LotCollect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateEnvoi;

    @ManyToOne
    private Utilisateur enqueteur;

    @ManyToOne
    private Projet projet;

    @Enumerated(EnumType.STRING)
    private StatusCollect statut;

    @OneToMany(mappedBy = "lotCollecte")
    private List<Collecte> collectes;
}

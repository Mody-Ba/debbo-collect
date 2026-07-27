package com.DebboCollect.DebboCollect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class Champ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeChamps type;

    private String options;

    private String question;

    private boolean preuveObligatoire;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_statistique")
    private TypeStatistique statistique;

    @ManyToOne
    @JoinColumn(name = "champ_parent_id")
    private Champ champParent;

    @Column(name = "valeur_declenchement")
    private String valeurDeclenchement;

    @ManyToOne
    private Projet projet;

    @OneToMany(mappedBy = "champ")
    private List<Reponse> reponses;
}

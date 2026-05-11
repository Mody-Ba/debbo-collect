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

    private String type;

    private String question;

    private boolean preuveObligatoire;

    @ManyToOne
    private Projet projet;

    @OneToMany(mappedBy = "champ")
    private List<Reponse> reponses;
}

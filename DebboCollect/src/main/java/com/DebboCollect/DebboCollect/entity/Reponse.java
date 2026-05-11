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
public class Reponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String valeur;

    @ManyToOne
    private Champ champ;

    @ManyToOne
    private Collecte collecte;

    @OneToMany(mappedBy = "reponse")
    private List<Media> medias;
}

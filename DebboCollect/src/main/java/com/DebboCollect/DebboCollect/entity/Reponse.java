package com.DebboCollect.DebboCollect.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

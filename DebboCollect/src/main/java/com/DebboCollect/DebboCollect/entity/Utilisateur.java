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
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String nom;

    private String email;

    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    private Boolean compteActif = false;

    @OneToMany(mappedBy = "expediteur")
    //plusieur message envoyer par un seul utli
    private List<Message> messagesEnvoyes;

    @OneToMany(mappedBy = "destinataire")
    //plusieur message recu par un seul utili
    private List<Message> messagesRecus;

    @OneToMany(mappedBy = "superviseur")
    private List<Projet> projetsSupervises;

    @ManyToOne
    private Projet projet;

    @ManyToOne
    private Utilisateur superviseur;

    @OneToMany(mappedBy = "bailleur")
    private List<Projet> projetsFinances;

    @OneToMany(mappedBy = "enqueteur")
    private List<Collecte> collectes;
}

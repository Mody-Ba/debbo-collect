package com.DebboCollect.DebboCollect.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

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
    @Column(nullable = false, unique = true)
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

    @ManyToMany
    @JoinTable(
            name = "superviseur_bailleur",
            joinColumns = @JoinColumn(name = "superviseur_id"),
            inverseJoinColumns = @JoinColumn(name = "bailleur_id")
    )
    private Set<Utilisateur> bailleursAssocies = new HashSet<>();

    @ManyToMany(mappedBy = "bailleursAssocies")
    private Set<Utilisateur> superviseursAssocies = new HashSet<>();



    @OneToMany(mappedBy = "bailleur")
    private List<Projet> projetsFinances;

    @OneToMany(mappedBy = "enqueteur")
    private List<Collecte> collectes;
}

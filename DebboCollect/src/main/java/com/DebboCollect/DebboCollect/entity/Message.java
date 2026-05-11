package com.DebboCollect.DebboCollect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenu;

    private Date dateEnvoi;

    private boolean lu;

    @ManyToOne
    // un utilisateur peut envoyer plusieur message
    private Utilisateur expediteur;

    @ManyToOne
    //un utilisateur peut recevoir plusieur message
    private Utilisateur destinataire;
}

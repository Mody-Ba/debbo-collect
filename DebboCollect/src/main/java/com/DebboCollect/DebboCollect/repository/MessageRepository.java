package com.DebboCollect.DebboCollect.repository;

import com.DebboCollect.DebboCollect.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
            SELECT m
            FROM Message m
            WHERE
            (
                m.expediteur.id = :utilisateurConnecte
                AND
                m.destinataire.id = :autreUtilisateur
            )
            OR
            (
                m.expediteur.id = :autreUtilisateur
                AND
                m.destinataire.id = :utilisateurConnecte
            )
            ORDER BY m.dateEnvoi ASC
            """)
    List<Message> trouverConversation(
            @Param("utilisateurConnecte") Long utilisateurConnecte,
            @Param("autreUtilisateur") Long autreUtilisateur
    );


    List<Message> findByExpediteurIdOrDestinataireIdOrderByDateEnvoiDesc(
            Long expediteurId,
            Long destinataireId
    );

    @Query("""
        SELECT COUNT(m)
        FROM Message m
        WHERE m.destinataire.id = :utilisateurId
        AND m.lu = false
        """)
    Long compterMessagesNonLus(
            @Param("utilisateurId") Long utilisateurId
    );

}
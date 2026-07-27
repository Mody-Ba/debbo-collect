package com.DebboCollect.DebboCollect.repository;

import com.DebboCollect.DebboCollect.entity.Role;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByEmail(String email);

    Optional<Utilisateur> findByEmailIgnoreCase(String email);

    boolean existsByRole(Role role);

    Integer countByProjetIdAndRole(Long projetId, Role role);


    Integer countByRoleAndSuperviseurEmail(
            Role role,
            String email
    );

    @Query("""
    SELECT COUNT(superviseur)
    FROM Utilisateur superviseur
    JOIN superviseur.bailleursAssocies bailleur
    WHERE bailleur.id = :bailleurId
""")
    long countSuperviseursAssociesAuBailleur(
            @Param("bailleurId") Long bailleurId
    );

}

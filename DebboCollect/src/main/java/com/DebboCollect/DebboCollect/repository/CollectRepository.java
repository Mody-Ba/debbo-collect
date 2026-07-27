package com.DebboCollect.DebboCollect.repository;

import com.DebboCollect.DebboCollect.entity.Collecte;
import com.DebboCollect.DebboCollect.entity.StatusCollect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CollectRepository extends JpaRepository<Collecte, Long> {

    Integer countByProjetId(Long projetId);

    List<Collecte> findByProjetId(Long projetId);

    Integer countByProjetIdAndStatut(
            Long projetId,
            StatusCollect statut
    );

    Integer countByProjetIdAndDateCollecte(
            Long projetId,
            LocalDate dateCollecte
    );

    Integer countByProjetSuperviseurEmail(String email);

    Integer countByEnqueteurEmail(String email);

    Integer countByEnqueteur_Email(String email);

}
package com.DebboCollect.DebboCollect.repository;

import com.DebboCollect.DebboCollect.entity.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {
    List<Projet> findByBailleurIdAndDateEnvoiBailleurIsNotNull(Long bailleurId);
    Optional<Projet> findByIdAndSuperviseurEmail(
            Long id,
            String email
    );

    boolean existsBySuperviseurIdAndBailleurId(
            Long superviseurId,
            Long bailleurId
    );

    Optional<Projet> findByIdAndBailleurEmail(Long id, String email);

    Integer countBySuperviseurEmail(String email);
}

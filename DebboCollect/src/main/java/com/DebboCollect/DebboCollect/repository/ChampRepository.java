package com.DebboCollect.DebboCollect.repository;

import com.DebboCollect.DebboCollect.entity.Champ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChampRepository extends JpaRepository<Champ, Long> {
    List<Champ> findByProjetId(Long projetId);
    Integer countByProjetId(Long projetId);

    @Query("""
SELECT COUNT(ch)
FROM Champ ch
WHERE ch.projet.id IN (
    SELECT DISTINCT c.projet.id
    FROM Collecte c
    WHERE c.enqueteur.email = :email
)
""")
    Integer countQuestionsByEnqueteurEmail(
            @Param("email") String email
    );


    @Query("""
SELECT DISTINCT ch
FROM Champ ch
WHERE ch.projet.id IN (
    SELECT DISTINCT c.projet.id
    FROM Collecte c
    WHERE c.enqueteur.email = :email
)
ORDER BY ch.id
""")
    List<Champ> findQuestionsByEnqueteurEmail(
            @Param("email") String email
    );


}
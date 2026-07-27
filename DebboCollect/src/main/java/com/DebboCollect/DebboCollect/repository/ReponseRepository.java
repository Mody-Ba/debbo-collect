package com.DebboCollect.DebboCollect.repository;

import com.DebboCollect.DebboCollect.entity.Reponse;
import com.DebboCollect.DebboCollect.projection.LocalisationProjection;
import com.DebboCollect.DebboCollect.projection.ReponseStatProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReponseRepository extends JpaRepository<Reponse, Long> {
    List<Reponse> findByCollecteIdOrderByChampIdAsc(Long collecteId);

    List<Reponse> findByCollecteId(Long collecteId);

    @Query("""
SELECT
r.champ.id AS champId,
r.valeur AS valeur,
COUNT(r) AS nombre
FROM Reponse r
WHERE r.collecte.projet.id = :projetId
AND (
    :region IS NULL
    OR EXISTS (
        SELECT 1
        FROM Reponse rr
        WHERE rr.collecte.id = r.collecte.id
        AND rr.champ.statistique = com.DebboCollect.DebboCollect.entity.TypeStatistique.REGION
        AND rr.valeur = :region
    )
)
GROUP BY r.champ.id, r.valeur
ORDER BY r.champ.id
""")
    List<ReponseStatProjection> getStatistiquesQuestions(
            @Param("projetId") Long projetId,
            @Param("region") String region
    );

    @Query("""
SELECT
r.champ.id AS champId,
r.valeur AS valeur,
COUNT(r) AS nombre
FROM Reponse r
WHERE r.collecte.enqueteur.email = :email
AND (
    :region IS NULL
    OR :region = ''
    OR EXISTS (
        SELECT 1
        FROM Reponse rr
        WHERE rr.collecte.id = r.collecte.id
        AND rr.champ.statistique = com.DebboCollect.DebboCollect.entity.TypeStatistique.REGION
        AND rr.valeur = :region
    )
)
GROUP BY r.champ.id, r.valeur
ORDER BY r.champ.id
""")
    List<ReponseStatProjection> getStatistiquesQuestionsEnqueteur(
            @Param("email") String email,
            @Param("region") String region
    );


    @Query("""
SELECT COUNT(r)
FROM Reponse r
WHERE r.collecte.projet.id = :projetId
""")
    Integer countTotalReponses(
            @Param("projetId") Long projetId
    );


    @Query("""
SELECT
r.valeur AS region,
COUNT(r.id) AS nombre
FROM Reponse r
WHERE r.collecte.projet.id = :projetId
AND r.champ.statistique = com.DebboCollect.DebboCollect.entity.TypeStatistique.REGION
AND (
    :region IS NULL
    OR :region = ''
    OR r.valeur = :region
)
GROUP BY r.valeur
ORDER BY COUNT(r.id) DESC
""")
    List<LocalisationProjection> getRegionsProjet(
            @Param("projetId") Long projetId,
            @Param("region") String region
    );

    @Query("""
SELECT COUNT(DISTINCT r.collecte.id)
FROM Reponse r
WHERE r.collecte.projet.id = :projetId
AND (
    :region IS NULL
    OR :region = ''
    OR EXISTS (
        SELECT 1
        FROM Reponse rr
        WHERE rr.collecte.id = r.collecte.id
        AND rr.champ.statistique = com.DebboCollect.DebboCollect.entity.TypeStatistique.REGION
        AND rr.valeur = :region
    )
)
""")
    Integer countCollectesParRegion(
            @Param("projetId") Long projetId,
            @Param("region") String region
    );

    @Query("""
SELECT COUNT(DISTINCT r.collecte.id)
FROM Reponse r
WHERE r.collecte.enqueteur.email = :email
AND (
    :region IS NULL
    OR :region = ''
    OR EXISTS (
        SELECT 1
        FROM Reponse rr
        WHERE rr.collecte.id = r.collecte.id
        AND rr.champ.statistique = com.DebboCollect.DebboCollect.entity.TypeStatistique.REGION
        AND rr.valeur = :region
    )
)
""")
    Integer countCollectesParRegionEnqueteur(
            @Param("email") String email,
            @Param("region") String region
    );


    @Query("""
SELECT COUNT(DISTINCT r.collecte.id)
FROM Reponse r
WHERE r.collecte.projet.id = :projetId
AND r.collecte.statut = com.DebboCollect.DebboCollect.entity.StatusCollect.VALIDEE
AND (
    :region IS NULL
    OR :region = ''
    OR EXISTS (
        SELECT 1
        FROM Reponse rr
        WHERE rr.collecte.id = r.collecte.id
        AND rr.champ.statistique = com.DebboCollect.DebboCollect.entity.TypeStatistique.REGION
        AND rr.valeur = :region
    )
)
""")
    Integer countCollectesValideesParRegion(
            @Param("projetId") Long projetId,
            @Param("region") String region
    );

    @Query("""
SELECT COUNT(DISTINCT r.collecte.id)
FROM Reponse r
WHERE r.collecte.enqueteur.email = :email
AND r.collecte.statut = com.DebboCollect.DebboCollect.entity.StatusCollect.VALIDEE
AND (
    :region IS NULL
    OR :region = ''
    OR EXISTS (
        SELECT 1
        FROM Reponse rr
        WHERE rr.collecte.id = r.collecte.id
        AND rr.champ.statistique = com.DebboCollect.DebboCollect.entity.TypeStatistique.REGION
        AND rr.valeur = :region
    )
)
""")
    Integer countCollectesValideesParRegionEnqueteur(
            @Param("email") String email,
            @Param("region") String region
    );


    @Query("""
SELECT
r.valeur AS valeur,
COUNT(r.id) AS nombre
FROM Reponse r
WHERE r.collecte.projet.id = :projetId
AND r.champ.statistique = com.DebboCollect.DebboCollect.entity.TypeStatistique.SEXE
AND (
    :region IS NULL
    OR :region = ''
    OR EXISTS (
        SELECT 1
        FROM Reponse rr
        WHERE rr.collecte.id = r.collecte.id
        AND rr.champ.statistique = com.DebboCollect.DebboCollect.entity.TypeStatistique.REGION
        AND rr.valeur = :region
    )
)
GROUP BY r.valeur
""")
    List<ReponseStatProjection> getStatistiquesSexe(
            @Param("projetId") Long projetId,
            @Param("region") String region
    );


    @Query("""
SELECT
r.valeur AS valeur,
COUNT(r.id) AS nombre
FROM Reponse r
WHERE r.collecte.projet.id = :projetId
AND r.champ.statistique = com.DebboCollect.DebboCollect.entity.TypeStatistique.AGE
AND (
    :region IS NULL
    OR :region = ''
    OR EXISTS (
        SELECT 1
        FROM Reponse rr
        WHERE rr.collecte.id = r.collecte.id
        AND rr.champ.statistique = com.DebboCollect.DebboCollect.entity.TypeStatistique.REGION
        AND rr.valeur = :region
    )
)
GROUP BY r.valeur
""")
    List<ReponseStatProjection> getStatistiquesAge(
            @Param("projetId") Long projetId,
            @Param("region") String region
    );


    @Query("""
SELECT r
FROM Reponse r
WHERE r.collecte.projet.id = :projetId
AND (
    :region IS NULL
    OR :region = ''
    OR EXISTS (
        SELECT 1
        FROM Reponse rr
        WHERE rr.collecte.id = r.collecte.id
        AND rr.champ.statistique = com.DebboCollect.DebboCollect.entity.TypeStatistique.REGION
        AND rr.valeur = :region
    )
)
AND (
    r.champ.type = com.DebboCollect.DebboCollect.entity.TypeChamps.CHOIX_UNIQUE
    OR r.champ.type = com.DebboCollect.DebboCollect.entity.TypeChamps.CHOIX_MULTIPLE
)
AND (
    r.champ.statistique IS NULL
    OR r.champ.statistique = com.DebboCollect.DebboCollect.entity.TypeStatistique.AUCUNE
)
""")
    List<Reponse> getReponsesChoixDashboard(
            @Param("projetId") Long projetId,
            @Param("region") String region
    );

    @Query("""
SELECT
r.valeur AS region,
COUNT(r.id) AS nombre
FROM Reponse r
WHERE r.collecte.enqueteur.email = :email
AND r.champ.statistique = com.DebboCollect.DebboCollect.entity.TypeStatistique.REGION
AND (
    :region IS NULL
    OR :region = ''
    OR r.valeur = :region
)
GROUP BY r.valeur
ORDER BY COUNT(r.id) DESC
""")
    List<LocalisationProjection> getCollectesParRegionEnqueteur(
            @Param("email") String email,
            @Param("region") String region
    );






}
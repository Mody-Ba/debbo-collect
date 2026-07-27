package com.DebboCollect.DebboCollect.services;
import com.DebboCollect.DebboCollect.Model.*;
import com.DebboCollect.DebboCollect.entity.*;

import com.DebboCollect.DebboCollect.projection.LocalisationProjection;
import com.DebboCollect.DebboCollect.projection.ReponseStatProjection;
import com.DebboCollect.DebboCollect.repository.ChampRepository;
import com.DebboCollect.DebboCollect.repository.CollectRepository;
import com.DebboCollect.DebboCollect.repository.ReponseRepository;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class StatistiqueServiceImpl  implements StatistiqueService {
    private final CollectRepository collectRepository;
    private final ChampRepository champRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ReponseRepository reponseRepository;
    private final ProjetRepository projetRepository;


    @Override
    public DashboardStatModel getDashboard(Long projetId, String region) {

        Integer totalCollectes =
                reponseRepository.countCollectesParRegion(
                        projetId,
                        region
                );

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Projet projet = projetRepository
                .findByIdAndSuperviseurEmail(projetId, email)
                .or(() -> projetRepository.findByIdAndBailleurEmail(projetId, email))
                .orElseThrow(() ->
                        new RuntimeException("Accès refusé à ce projet"));

        Integer collectesValidees =
                reponseRepository.countCollectesValideesParRegion(
                        projetId,
                        region
                );

        Integer collectesEnAttente = collectRepository.countByProjetIdAndStatut(
                projetId,
                StatusCollect.EN_ATTENTE
        );

        Integer collectesRevision = collectRepository.countByProjetIdAndStatut(
                projetId,
                StatusCollect.EN_REVISION
        );

        Integer collectesEnvoyees = collectRepository.countByProjetIdAndStatut(
                projetId,
                StatusCollect.ENVOYEE
        );

        Integer collectesEnregistrees = collectRepository.countByProjetIdAndStatut(
                projetId,
                StatusCollect.ENREGISTREE
        );

        Integer nombreQuestions =
                champRepository.countByProjetId(projetId);

        Integer nombreEnqueteurs =
                utilisateurRepository.countByProjetIdAndRole(
                        projetId,
                        Role.ENQUETEUR
                );

        // ==========================
        // Statistiques Sexe
        // ==========================

        List<ReponseStatProjection> statistiquesSexe = reponseRepository.getStatistiquesSexe(projetId, region);

        Integer nombreHommes = 0;
        Integer nombreFemmes = 0;

        for (ReponseStatProjection stat : statistiquesSexe) {

            if ("Homme".equalsIgnoreCase(stat.getValeur())
                    || "Homme ".equalsIgnoreCase(stat.getValeur())) {

                nombreHommes = stat.getNombre().intValue();
            }

            if ("Femme".equalsIgnoreCase(stat.getValeur())
                    || "Femme ".equalsIgnoreCase(stat.getValeur())) {

                nombreFemmes = stat.getNombre().intValue();
            }
        }

        // ==========================
// Statistiques Age
// ==========================

        List<ReponseStatProjection> statistiquesAge =
                reponseRepository.getStatistiquesAge(
                        projetId,
                        region
                );

        Integer nombreEnfants = 0;
        Integer nombreAdultes = 0;
        Integer nombrePersonnesAgees = 0;

        for (ReponseStatProjection stat : statistiquesAge) {

            try {

                int age = Integer.parseInt(stat.getValeur().trim());

                if (age < 18) {

                    nombreEnfants += stat.getNombre().intValue();

                } else if (age < 60) {

                    nombreAdultes += stat.getNombre().intValue();

                } else {

                    nombrePersonnesAgees += stat.getNombre().intValue();

                }

            } catch (Exception e) {
                // Ignore les valeurs invalides
            }
        }

        double pourcentageValidation = 0;
        double pourcentageEnAttente = 0;
        double pourcentageRevision = 0;
        double pourcentageEnvoyees = 0;
        double pourcentageEnregistrees = 0;

        if (totalCollectes > 0) {

            pourcentageValidation =
                    (collectesValidees * 100.0) / totalCollectes;

            pourcentageEnAttente =
                    (collectesEnAttente * 100.0) / totalCollectes;

            pourcentageRevision =
                    (collectesRevision * 100.0) / totalCollectes;

            pourcentageEnvoyees =
                    (collectesEnvoyees * 100.0) / totalCollectes;

            pourcentageEnregistrees =
                    (collectesEnregistrees * 100.0) / totalCollectes;
        }

        boolean aSexe =
                nombreHommes > 0 || nombreFemmes > 0;

        boolean aAge =
                nombreEnfants > 0 ||
                        nombreAdultes > 0 ||
                        nombrePersonnesAgees > 0;

        List<DashboardCardModel> cartesDynamiques =
                new ArrayList<>();

        if (!aSexe && !aAge) {

            cartesDynamiques =
                    calculerCartesDynamiques(
                            projetId,
                            region,
                            totalCollectes
                    );
        }

        return DashboardStatModel.builder()
                .totalCollectes(totalCollectes)
                .collectesValidees(collectesValidees)
                .collectesEnAttente(collectesEnAttente)
                .collectesRevision(collectesRevision)
                .collectesEnvoyees(collectesEnvoyees)
                .collectesEnregistrees(collectesEnregistrees)
                .cartesDynamiques(cartesDynamiques)

                .nombreQuestions(nombreQuestions)
                .nombreEnqueteurs(nombreEnqueteurs)

                // Nouveaux compteurs
                .nombreHommes(nombreHommes)
                .nombreFemmes(nombreFemmes)

                .nombreEnfants(nombreEnfants)
                .nombreAdultes(nombreAdultes)
                .nombrePersonnesAgees(nombrePersonnesAgees)

                .pourcentageValidation(
                        Math.round(pourcentageValidation * 10) / 10.0
                )
                .pourcentageEnAttente(
                        Math.round(pourcentageEnAttente * 10) / 10.0
                )
                .pourcentageRevision(
                        Math.round(pourcentageRevision * 10) / 10.0
                )
                .pourcentageEnvoyees(
                        Math.round(pourcentageEnvoyees * 10) / 10.0
                )
                .pourcentageEnregistrees(
                        Math.round(pourcentageEnregistrees * 10) / 10.0
                )

                .build();
    }

    @Override
    public List<QuestionStatModel> getStatistiquesQuestions(Long projetId, String region) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        projetRepository
                .findByIdAndSuperviseurEmail(projetId, email)
                .or(() -> projetRepository.findByIdAndBailleurEmail(projetId, email))
                .orElseThrow(() ->
                        new RuntimeException("Accès refusé à ce projet"));

        List<ReponseStatProjection> statistiques =
                reponseRepository.getStatistiquesQuestions(
                        projetId,
                        region
                );

        List<Champ> champs =
                champRepository.findByProjetId(projetId);

        Map<Long, QuestionStatModel> mapQuestions = new HashMap<>();

        for (Champ champ : champs) {

            mapQuestions.put(
                    champ.getId(),
                    QuestionStatModel.builder()
                            .champId(champ.getId())
                            .question(champ.getQuestion())
                            .type(champ.getType())
                            .statistique(champ.getStatistique())
                            .reponses(new ArrayList<>())
                            .build()
            );

        }

        for (ReponseStatProjection stat : statistiques) {

            QuestionStatModel question =
                    mapQuestions.get(stat.getChampId());

            if (question == null) {
                continue;
            }

            if (question.getType() != null &&
                    question.getType().name().equals("CHOIX_MULTIPLE")) {

                if (stat.getValeur() == null || stat.getValeur().isBlank()) {
                    continue;
                }

                String[] valeurs = stat.getValeur().split(";");

                for (String valeur : valeurs) {

                    String valeurNettoyee = valeur.trim();

                    ReponseStatModel existante = question.getReponses()
                            .stream()
                            .filter(r -> r.getValeur().equals(valeurNettoyee))
                            .findFirst()
                            .orElse(null);

                    if (existante == null) {

                        question.getReponses().add(
                                ReponseStatModel.builder()
                                        .valeur(valeurNettoyee)
                                        .nombre(stat.getNombre())
                                        .build()
                        );

                    } else {

                        existante.setNombre(
                                existante.getNombre() + stat.getNombre()
                        );

                    }

                }

            } else {

                question.getReponses().add(
                        ReponseStatModel.builder()
                                .valeur(stat.getValeur())
                                .nombre(stat.getNombre())
                                .build()
                );

            }

        }

        return new ArrayList<>(mapQuestions.values());

    }

    @Override
    public List<LocalisationStatModel> getCollectesParRegion(
            Long projetId,
            String region
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        projetRepository
                .findByIdAndSuperviseurEmail(projetId, email)
                .or(() -> projetRepository.findByIdAndBailleurEmail(projetId, email))
                .orElseThrow(() ->
                        new RuntimeException("Accès refusé à ce projet"));

        var regions = reponseRepository.getRegionsProjet(
                projetId,
                region
        );

        return regions.stream()
                .map(r ->
                        LocalisationStatModel.builder()
                                .region(r.getRegion())
                                .nombre(r.getNombre())
                                .build()
                )
                .toList();
    }





    @Override
    public DashboardAccueilModel getDashboardAccueil() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Integer nombreProjets =
                projetRepository.countBySuperviseurEmail(email);

        Integer nombreEnqueteurs =
                utilisateurRepository.countByRoleAndSuperviseurEmail(
                        Role.ENQUETEUR,
                        email
                );

        Integer nombreBailleurs =
                utilisateurRepository.countByRoleAndSuperviseurEmail(
                        Role.BAILLEUR,
                        email
                );

        Integer nombreCollectes =
                collectRepository.countByProjetSuperviseurEmail(email);

        return DashboardAccueilModel.builder()
                .nombreProjets(nombreProjets)
                .nombreEnqueteurs(nombreEnqueteurs)
                .nombreBailleurs(nombreBailleurs)
                .nombreCollectes(nombreCollectes)
                .build();
    }


    @Override
    public DashboardStatModel getDashboardEnqueteur(String region) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Integer totalCollectes =
                reponseRepository.countCollectesParRegionEnqueteur(
                        email,
                        region
                );

        Integer collectesValidees =
                reponseRepository.countCollectesValideesParRegionEnqueteur(
                        email,
                        region
                );

        Integer nombreQuestions =
                champRepository.countQuestionsByEnqueteurEmail(email);

        double pourcentageValidation = 0;

        if (totalCollectes > 0) {

            pourcentageValidation =
                    (collectesValidees * 100.0) / totalCollectes;

        }

        return DashboardStatModel.builder()
                .totalCollectes(totalCollectes)
                .collectesValidees(collectesValidees)
                .nombreQuestions(nombreQuestions)
                .pourcentageValidation(
                        Math.round(pourcentageValidation * 10) / 10.0
                )
                .build();
    }

    @Override
    public List<QuestionStatModel> getStatistiquesQuestionsEnqueteur(String region) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        List<ReponseStatProjection> statistiques =
                reponseRepository.getStatistiquesQuestionsEnqueteur(
                        email,
                        region
                );

        List<Champ> champs =
                champRepository.findQuestionsByEnqueteurEmail(email);

        Map<Long, QuestionStatModel> mapQuestions = new HashMap<>();

        for (Champ champ : champs) {

            mapQuestions.put(
                    champ.getId(),
                    QuestionStatModel.builder()
                            .champId(champ.getId())
                            .question(champ.getQuestion())
                            .type(champ.getType())
                            .statistique(champ.getStatistique())
                            .reponses(new ArrayList<>())
                            .build()
            );

        }

        for (ReponseStatProjection stat : statistiques) {

            QuestionStatModel question =
                    mapQuestions.get(stat.getChampId());

            if (question == null) {
                continue;
            }

            if (question.getType() != null
                    && question.getType().name().equals("CHOIX_MULTIPLE")) {

                if (stat.getValeur() == null || stat.getValeur().isBlank()) {
                    continue;
                }

                String[] valeurs = stat.getValeur().split(";");

                for (String valeur : valeurs) {

                    String valeurNettoyee = valeur.trim();

                    ReponseStatModel existante = question.getReponses()
                            .stream()
                            .filter(r -> r.getValeur().equals(valeurNettoyee))
                            .findFirst()
                            .orElse(null);

                    if (existante == null) {

                        question.getReponses().add(
                                ReponseStatModel.builder()
                                        .valeur(valeurNettoyee)
                                        .nombre(stat.getNombre())
                                        .build()
                        );

                    } else {

                        existante.setNombre(
                                existante.getNombre() + stat.getNombre()
                        );

                    }

                }

            } else {

                question.getReponses().add(
                        ReponseStatModel.builder()
                                .valeur(stat.getValeur())
                                .nombre(stat.getNombre())
                                .build()
                );

            }

        }

        return new ArrayList<>(mapQuestions.values());

    }

    private List<DashboardCardModel> calculerCartesDynamiques(
            Long projetId,
            String region,
            Integer totalCollectes
    ) {

        List<Reponse> reponses =
                reponseRepository.getReponsesChoixDashboard(
                        projetId,
                        region
                );

        Map<String, Long> compteur = new HashMap<>();

        for (Reponse reponse : reponses) {

            if (reponse.getValeur() == null || reponse.getValeur().isBlank()) {
                continue;
            }

            String[] valeurs = reponse.getValeur().split(";");

            for (String valeur : valeurs) {

                String valeurNettoyee = valeur.trim();

                if (valeurNettoyee.isBlank()) {
                    continue;
                }

                compteur.put(
                        valeurNettoyee,
                        compteur.getOrDefault(valeurNettoyee, 0L) + 1
                );
            }
        }

        return compteur.entrySet()
                .stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(4)
                .map(entry -> {

                    double pourcentage = 0;

                    if (totalCollectes != null && totalCollectes > 0) {
                        pourcentage =
                                (entry.getValue() * 100.0) / totalCollectes;
                    }

                    return DashboardCardModel.builder()
                            .titre(entry.getKey())
                            .nombre(entry.getValue())
                            .pourcentage(
                                    Math.round(pourcentage * 10) / 10.0
                            )
                            .build();
                })
                .toList();
    }


    @Override
    public List<LocalisationProjection> getCollectesParRegionEnqueteur(String region) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return reponseRepository.getCollectesParRegionEnqueteur(
                email,
                region
        );
    }




}

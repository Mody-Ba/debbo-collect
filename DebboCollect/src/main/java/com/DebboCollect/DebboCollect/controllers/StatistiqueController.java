package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.*;
import com.DebboCollect.DebboCollect.projection.LocalisationProjection;
import com.DebboCollect.DebboCollect.services.StatistiqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistiques")
@RequiredArgsConstructor
public class StatistiqueController {

    private final StatistiqueService statistiqueService;

    @GetMapping("/dashboard/{projetId}")
    public DashboardStatModel dashboard(
            @PathVariable Long projetId,
            @RequestParam(required = false) String region
    ) {

        return statistiqueService.getDashboard(
                projetId,
                region
        );

    }

    @GetMapping("/questions/{projetId}")
    public List<QuestionStatModel> getStatistiquesQuestions(
            @PathVariable Long projetId,
            @RequestParam(required = false) String region
    ) {

        return statistiqueService.getStatistiquesQuestions(
                projetId,
                region
        );

    }

    @GetMapping("/localisation/{projetId}")
    @PreAuthorize("hasAnyRole('SUPERVISEUR','BAILLEUR')")
    public List<LocalisationStatModel> getCollectesParRegion(
            @PathVariable Long projetId,
            @RequestParam(required = false) String region
    ) {

        return statistiqueService.getCollectesParRegion(
                projetId,
                 region

        );

    }

    @GetMapping("/dashboard-accueil")
    @PreAuthorize("hasRole('SUPERVISEUR')")
    public DashboardAccueilModel getDashboardAccueil() {

        return statistiqueService.getDashboardAccueil();

    }
    @GetMapping("/dashboard-enqueteur")
    @PreAuthorize("hasRole('ENQUETEUR')")
    public DashboardStatModel getDashboardEnqueteur(
            @RequestParam(required = false) String region
    ) {
        return statistiqueService.getDashboardEnqueteur(region);
    }

    @GetMapping("/questions-enqueteur")
    @PreAuthorize("hasRole('ENQUETEUR')")
    public List<QuestionStatModel> getQuestionsEnqueteur(
            @RequestParam(required = false) String region
    ) {
        return statistiqueService.getStatistiquesQuestionsEnqueteur(region);
    }

    @GetMapping("/localisation-enqueteur")
    public List<LocalisationProjection> getLocalisationEnqueteur(
            @RequestParam(required = false) String region
    ) {
        return statistiqueService.getCollectesParRegionEnqueteur(region);
    }

}

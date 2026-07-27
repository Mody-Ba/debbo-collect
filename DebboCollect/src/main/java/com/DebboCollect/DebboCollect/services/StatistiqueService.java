package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.*;
import com.DebboCollect.DebboCollect.projection.LocalisationProjection;

import java.util.List;

public interface StatistiqueService {
    DashboardStatModel getDashboard(
            Long projetId,
            String region
    );

    List<QuestionStatModel> getStatistiquesQuestions(Long projetId , String region);

    List<LocalisationStatModel> getCollectesParRegion(Long projetId,String region);

    DashboardAccueilModel getDashboardAccueil();

    DashboardStatModel getDashboardEnqueteur(String region);


    List<QuestionStatModel> getStatistiquesQuestionsEnqueteur(String region);

    List<LocalisationProjection> getCollectesParRegionEnqueteur(String region);




}

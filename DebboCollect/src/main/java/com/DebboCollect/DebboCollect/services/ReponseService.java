package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.ReponseRequest;
import com.DebboCollect.DebboCollect.Model.ReponseResponse;

import java.util.List;

public interface ReponseService {

    ReponseResponse creerReponse(ReponseRequest request);

    List<ReponseResponse> afficherToutesLesReponses();

    ReponseResponse afficherReponseParId(Long id);

    ReponseResponse modifierReponse(Long id, ReponseRequest request);

    void supprimerReponse(Long id);
}
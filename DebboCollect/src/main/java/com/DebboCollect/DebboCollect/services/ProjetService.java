package com.DebboCollect.DebboCollect.services;


import com.DebboCollect.DebboCollect.Model.ProjetDashboardResponse;
import com.DebboCollect.DebboCollect.Model.ProjetRequest;
import com.DebboCollect.DebboCollect.Model.ProjetResponse;

import java.util.List;
import java.util.Map;

public interface ProjetService {

    ProjetResponse creerProjet(ProjetRequest request);

    List<ProjetResponse> afficherTousLesProjets();

    ProjetResponse afficherProjetParId(Long id);

    ProjetResponse modifierProjet(Long id, ProjetRequest request);

    void supprimerProjet(Long id);

    ProjetResponse assignerEnqueteur(
            Long projetId,
            Long enqueteurId
    );

    List<ProjetResponse> afficherMesProjets();

    List<Map<String, Object>> exporterProjet(Long projetId);

    ProjetResponse envoyerAuBailleur(Long id);

    List<ProjetResponse> getProjetsEnvoyesAuBailleur();

    ProjetDashboardResponse getDashboardProjet(Long projetId);

    ProjetResponse terminerProjet(Long id);


}

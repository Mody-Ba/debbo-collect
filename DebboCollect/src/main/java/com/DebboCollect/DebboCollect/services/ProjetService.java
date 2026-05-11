package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.ProjetRequest;
import com.DebboCollect.DebboCollect.Model.ProjetResponse;

import java.util.List;

public interface ProjetService {

    ProjetResponse creerProjet(ProjetRequest request);

    List<ProjetResponse> afficherTousLesProjets();

    ProjetResponse afficherProjetParId(Long id);

    ProjetResponse modifierProjet(Long id, ProjetRequest request);

    void supprimerProjet(Long id);
}

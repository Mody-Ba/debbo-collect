package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.ChampRequest;
import com.DebboCollect.DebboCollect.Model.ChampResponse;

import java.util.List;

public interface ChampService {

    ChampResponse creerChamp(ChampRequest request);

    List<ChampResponse> afficherTousLesChamps();

    ChampResponse afficherChampParId(Long id);

    ChampResponse modifierChamp(Long id, ChampRequest request);

    void supprimerChamp(Long id);
}

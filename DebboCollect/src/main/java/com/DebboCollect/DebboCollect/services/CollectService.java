package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.CollectRequest;
import com.DebboCollect.DebboCollect.Model.CollectResponse;
import com.DebboCollect.DebboCollect.Model.CollectRequest;
import com.DebboCollect.DebboCollect.Model.CollectResponse;

import java.util.List;

public interface CollectService {

    CollectResponse creerCollecte(CollectRequest request);

    List<CollectResponse> afficherToutesLesCollectes();

    CollectResponse afficherCollecteParId(Long id);

    CollectResponse modifierCollecte(Long id, CollectRequest request);

    void supprimerCollecte(Long id);
}
package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.LotCollectRequest;
import com.DebboCollect.DebboCollect.Model.LotCollectResponse;

import java.util.List;

public interface LotCollectService {

    LotCollectResponse creerLot(LotCollectRequest request);

    List<LotCollectResponse> afficherTousLesLots();

    LotCollectResponse afficherLotParId(Long id);
    LotCollectResponse validerLot(Long id);

    LotCollectResponse demanderRevisionLot(Long id);
}

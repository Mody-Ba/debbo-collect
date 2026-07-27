package com.DebboCollect.DebboCollect.controllers;
import com.DebboCollect.DebboCollect.Model.LotCollectRequest;
import com.DebboCollect.DebboCollect.Model.LotCollectResponse;
import com.DebboCollect.DebboCollect.services.LotCollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lots")
@RequiredArgsConstructor
public class LotCollectController {

    private final LotCollectService lotCollecteService;

    @PostMapping
    @PreAuthorize("hasRole('ENQUETEUR')")
    public LotCollectResponse creerLot(
            @RequestBody LotCollectRequest request
    ) {
        return lotCollecteService.creerLot(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPERVISEUR','ENQUETEUR')")
    public List<LotCollectResponse> afficherTousLesLots() {
        return lotCollecteService.afficherTousLesLots();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERVISEUR','ENQUETEUR')")
    public LotCollectResponse afficherLotParId(
            @PathVariable Long id
    ) {
        return lotCollecteService.afficherLotParId(id);
    }

    @PutMapping("/{id}/valider")
    public LotCollectResponse validerLot(
            @PathVariable Long id) {

        return lotCollecteService.validerLot(id);
    }

    @PutMapping("/{id}/revision")
    public LotCollectResponse demanderRevisionLot(
            @PathVariable Long id) {

        return lotCollecteService.demanderRevisionLot(id);
    }
}

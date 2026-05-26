package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.CollectRequest;
import com.DebboCollect.DebboCollect.Model.CollectResponse;
import com.DebboCollect.DebboCollect.services.CollectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collectes")
@RequiredArgsConstructor
public class CollectController {
    private final CollectService collecteService;

    @PostMapping
    @PreAuthorize("hasRole('ENQUETEUR')")
    public CollectResponse creerCollecte(@Valid @RequestBody CollectRequest request) {

        return collecteService.creerCollecte(request);
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPERVISEUR')")
    public List<CollectResponse> afficherToutesLesCollectes() {

        return collecteService.afficherToutesLesCollectes();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPERVISEUR')")
    public CollectResponse afficherCollecteParId(@PathVariable Long id) {

        return collecteService.afficherCollecteParId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ENQUETEUR')")
    public CollectResponse modifierCollecte(@PathVariable Long id,
                                             @RequestBody CollectRequest request) {

        return collecteService.modifierCollecte(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERVISEUR')")
    public void supprimerCollecte(@PathVariable Long id) {

        collecteService.supprimerCollecte(id);
    }

    @PutMapping("/{id}/valider")
    @PreAuthorize("hasRole('SUPERVISEUR')")
    public CollectResponse validerCollecte(@PathVariable Long id) {

        return collecteService.validerCollecte(id);
    }

    @PutMapping("/{id}/revision")
    @PreAuthorize("hasRole('SUPERVISEUR')")
    public CollectResponse demanderRevision(@PathVariable Long id) {

        return collecteService.demanderRevision(id);
    }



}

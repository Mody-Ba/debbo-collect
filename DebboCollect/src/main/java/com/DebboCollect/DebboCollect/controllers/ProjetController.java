package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.ProjetRequest;
import com.DebboCollect.DebboCollect.Model.ProjetResponse;
import com.DebboCollect.DebboCollect.services.ProjetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
@RequiredArgsConstructor
public class ProjetController {

    private final ProjetService projetService;

    @PostMapping
    @PreAuthorize("hasRole('SUPERVISEUR')")
    public ProjetResponse creerProjet(@Valid @RequestBody ProjetRequest request) {

        return projetService.creerProjet(request);
    }

    @PutMapping("/{projetId}/assigner-enqueteur/{enqueteurId}")
    @PreAuthorize("hasRole('SUPERVISEUR')")
    public ProjetResponse assignerEnqueteur(
            @PathVariable Long projetId,
            @PathVariable Long enqueteurId
    ) {

        return projetService.assignerEnqueteur(
                projetId,
                enqueteurId
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPERVISEUR')")
    public List<ProjetResponse> afficherTousLesProjets() {

        return projetService.afficherTousLesProjets();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERVISEUR','BAILLEUR')")
    public ProjetResponse afficherProjetParId(@PathVariable Long id) {

        return projetService.afficherProjetParId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPERVISEUR')")
    public ProjetResponse modifierProjet(@PathVariable Long id,
                                         @RequestBody ProjetRequest request) {

        return projetService.modifierProjet(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPERVISEUR')")
    public void supprimerProjet(@PathVariable Long id) {

        projetService.supprimerProjet(id);
    }
}

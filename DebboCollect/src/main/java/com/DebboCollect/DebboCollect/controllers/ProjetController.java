package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.ProjetRequest;
import com.DebboCollect.DebboCollect.Model.ProjetResponse;
import com.DebboCollect.DebboCollect.services.ProjetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
@RequiredArgsConstructor
public class ProjetController {

    private final ProjetService projetService;

    @PostMapping
    public ProjetResponse creerProjet(@Valid @RequestBody ProjetRequest request) {

        return projetService.creerProjet(request);
    }

    @GetMapping
    public List<ProjetResponse> afficherTousLesProjets() {

        return projetService.afficherTousLesProjets();
    }

    @GetMapping("/{id}")
    public ProjetResponse afficherProjetParId(@PathVariable Long id) {

        return projetService.afficherProjetParId(id);
    }

    @PutMapping("/{id}")
    public ProjetResponse modifierProjet(@PathVariable Long id,
                                         @RequestBody ProjetRequest request) {

        return projetService.modifierProjet(id, request);
    }

    @DeleteMapping("/{id}")
    public void supprimerProjet(@PathVariable Long id) {

        projetService.supprimerProjet(id);
    }
}

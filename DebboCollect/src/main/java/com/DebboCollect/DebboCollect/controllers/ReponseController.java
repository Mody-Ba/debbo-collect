package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.ReponseRequest;
import com.DebboCollect.DebboCollect.Model.ReponseResponse;
import com.DebboCollect.DebboCollect.services.ReponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reponses")
@RequiredArgsConstructor
public class ReponseController {

    private final ReponseService reponseService;

    @PostMapping
    @PreAuthorize("hasRole('ENQUETEUR')")
    public ReponseResponse creerReponse(@Valid @RequestBody ReponseRequest request) {

        return reponseService.creerReponse(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR')")
    public List<ReponseResponse> afficherToutesLesReponses() {

        return reponseService.afficherToutesLesReponses();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR')")
    public ReponseResponse afficherReponseParId(@PathVariable Long id) {

        return reponseService.afficherReponseParId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ENQUETEUR')")
    public ReponseResponse modifierReponse(@PathVariable Long id,
                                           @RequestBody ReponseRequest request) {

        return reponseService.modifierReponse(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void supprimerReponse(@PathVariable Long id) {

        reponseService.supprimerReponse(id);
    }
}
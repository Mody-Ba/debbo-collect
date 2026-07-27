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
    @PreAuthorize("hasAnyRole('ENQUETEUR','SUPERVISEUR')")
    public List<ReponseResponse> afficherToutesLesReponses() {

        return reponseService.afficherToutesLesReponses();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ENQUETEUR','SUPERVISEUR')")
    public ReponseResponse afficherReponseParId(@PathVariable Long id) {

        return reponseService.afficherReponseParId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ENQUETEUR')")
    public ReponseResponse modifierReponse(@PathVariable Long id,
                                           @RequestBody ReponseRequest request) {

        return reponseService.modifierReponse(id, request);
    }

    @GetMapping("/collecte/{collecteId}")
    @PreAuthorize("hasAnyRole('ENQUETEUR','SUPERVISEUR')")
    public List<ReponseResponse> afficherReponsesParCollecte(
            @PathVariable Long collecteId) {

        return reponseService.afficherReponsesParCollecte(collecteId);
    }

    @PutMapping("/{id}/commentaire")
    @PreAuthorize("hasRole('SUPERVISEUR')")
    public ReponseResponse ajouterCommentaire(
            @PathVariable Long id,
            @RequestBody String commentaire
    ) {

        return reponseService.ajouterCommentaire(
                id,
                commentaire
        );
    }


}
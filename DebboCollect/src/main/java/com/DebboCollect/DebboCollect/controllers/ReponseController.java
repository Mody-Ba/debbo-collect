package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.ReponseRequest;
import com.DebboCollect.DebboCollect.Model.ReponseResponse;
import com.DebboCollect.DebboCollect.services.ReponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reponses")
@RequiredArgsConstructor
public class ReponseController {

    private final ReponseService reponseService;

    @PostMapping
    public ReponseResponse creerReponse(@Valid @RequestBody ReponseRequest request) {

        return reponseService.creerReponse(request);
    }

    @GetMapping
    public List<ReponseResponse> afficherToutesLesReponses() {

        return reponseService.afficherToutesLesReponses();
    }

    @GetMapping("/{id}")
    public ReponseResponse afficherReponseParId(@PathVariable Long id) {

        return reponseService.afficherReponseParId(id);
    }

    @PutMapping("/{id}")
    public ReponseResponse modifierReponse(@PathVariable Long id,
                                           @RequestBody ReponseRequest request) {

        return reponseService.modifierReponse(id, request);
    }

    @DeleteMapping("/{id}")
    public void supprimerReponse(@PathVariable Long id) {

        reponseService.supprimerReponse(id);
    }
}
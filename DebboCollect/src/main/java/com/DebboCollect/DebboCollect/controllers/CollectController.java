package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.CollectRequest;
import com.DebboCollect.DebboCollect.Model.CollectResponse;
import com.DebboCollect.DebboCollect.services.CollectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collectes")
@RequiredArgsConstructor
public class CollectController {
    private final CollectService collecteService;

    @PostMapping
    public CollectResponse creerCollecte(@Valid @RequestBody CollectRequest request) {

        return collecteService.creerCollecte(request);
    }

    @GetMapping
    public List<CollectResponse> afficherToutesLesCollectes() {

        return collecteService.afficherToutesLesCollectes();
    }

    @GetMapping("/{id}")
    public CollectResponse afficherCollecteParId(@PathVariable Long id) {

        return collecteService.afficherCollecteParId(id);
    }

    @PutMapping("/{id}")
    public CollectResponse modifierCollecte(@PathVariable Long id,
                                             @RequestBody CollectRequest request) {

        return collecteService.modifierCollecte(id, request);
    }

    @DeleteMapping("/{id}")
    public void supprimerCollecte(@PathVariable Long id) {

        collecteService.supprimerCollecte(id);
    }

}

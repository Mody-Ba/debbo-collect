package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.ChampRequest;
import com.DebboCollect.DebboCollect.Model.ChampResponse;
import com.DebboCollect.DebboCollect.services.ChampService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/champs")
@RequiredArgsConstructor
public class ChampController {

    private final ChampService champService;

    @PostMapping
    public ChampResponse creerChamp(@Valid @RequestBody ChampRequest request) {

        return champService.creerChamp(request);
    }

    @GetMapping
    public List<ChampResponse> afficherTousLesChamps() {

        return champService.afficherTousLesChamps();
    }

    @GetMapping("/{id}")
    public ChampResponse afficherChampParId(@PathVariable Long id) {

        return champService.afficherChampParId(id);
    }

    @PutMapping("/{id}")
    public ChampResponse modifierChamp(@PathVariable Long id,
                                       @RequestBody ChampRequest request) {

        return champService.modifierChamp(id, request);
    }

    @DeleteMapping("/{id}")
    public void supprimerChamp(@PathVariable Long id) {

        champService.supprimerChamp(id);
    }
}
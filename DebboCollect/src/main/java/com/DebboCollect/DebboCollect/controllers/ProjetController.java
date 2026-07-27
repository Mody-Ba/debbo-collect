package com.DebboCollect.DebboCollect.controllers;


import com.DebboCollect.DebboCollect.Model.ProjetDashboardResponse;
import com.DebboCollect.DebboCollect.Model.ProjetRequest;
import com.DebboCollect.DebboCollect.Model.ProjetResponse;
import com.DebboCollect.DebboCollect.services.ProjetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.DebboCollect.DebboCollect.services.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projets")
@RequiredArgsConstructor
public class ProjetController {

    private final ProjetService projetService;

    private final ExcelService excelService;

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
    @PreAuthorize("hasAnyRole('SUPERVISEUR','ENQUETEUR','BAILLEUR')")
    public List<ProjetResponse> afficherTousLesProjets() {

        return projetService.afficherTousLesProjets();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERVISEUR','BAILLEUR','ENQUETEUR')")
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
    @GetMapping("/mes-projets")
    @PreAuthorize("hasRole('ENQUETEUR')")
    public List<ProjetResponse> afficherMesProjets() {

        return projetService.afficherMesProjets();
    }
    @GetMapping("/{id}/export")
    public ResponseEntity<List<Map<String, Object>>> exporterProjet(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                projetService.exporterProjet(id)
        );
    }

    @GetMapping("/{id}/excel")
    public ResponseEntity<ByteArrayResource> exporterExcel(
            @PathVariable Long id) {

        byte[] fichier = excelService.exporterProjetExcel(id);

        ByteArrayResource resource =
                new ByteArrayResource(fichier);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=Projet_" + id + ".xlsx"
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .contentLength(fichier.length)
                .body(resource);
    }

    @PutMapping("/{id}/envoyer-bailleur")
    @PreAuthorize("hasRole('SUPERVISEUR')")
    public ProjetResponse envoyerAuBailleur(
            @PathVariable Long id) {

        return projetService.envoyerAuBailleur(id);
    }

    @GetMapping("/bailleur")
    public ResponseEntity<List<ProjetResponse>> getMesProjets() {

        return ResponseEntity.ok(
                projetService.getProjetsEnvoyesAuBailleur()
        );
    }

    @GetMapping("/{id}/dashboard")
    @PreAuthorize("hasRole('BAILLEUR')")
    public ResponseEntity<ProjetDashboardResponse> dashboardProjet(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                projetService.getDashboardProjet(id)
        );
    }

    @PutMapping("/{id}/terminer")
    @PreAuthorize("hasRole('SUPERVISEUR')")
    public ProjetResponse terminerProjet(
            @PathVariable Long id
    ) {

        return projetService.terminerProjet(id);
    }
}

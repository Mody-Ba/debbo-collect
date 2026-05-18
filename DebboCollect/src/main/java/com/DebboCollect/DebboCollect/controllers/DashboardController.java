package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.DashboardResponse;
import com.DebboCollect.DebboCollect.repository.CollectRepository;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import com.DebboCollect.DebboCollect.repository.ReponseRepository;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistiques")
@RequiredArgsConstructor
public class DashboardController {
    private final ProjetRepository projetRepository;
    private final CollectRepository collecteRepository;
    private final ReponseRepository reponseRepository;
    private final UtilisateurRepository utilisateurRepository;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public DashboardResponse getDashboard() {

        return DashboardResponse.builder()
                .nombreProjets(projetRepository.count())
                .nombreCollectes(collecteRepository.count())
                .nombreReponses(reponseRepository.count())
                .nombreUtilisateurs(utilisateurRepository.count())
                .build();
    }
}

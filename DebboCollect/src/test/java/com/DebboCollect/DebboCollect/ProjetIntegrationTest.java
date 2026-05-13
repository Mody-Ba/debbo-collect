package com.DebboCollect.DebboCollect;
import com.DebboCollect.DebboCollect.Model.ProjetRequest;
import com.DebboCollect.DebboCollect.Model.ProjetResponse;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import com.DebboCollect.DebboCollect.services.ProjetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")

public class ProjetIntegrationTest {
    @Autowired
    private ProjetServiceImpl projetService;

    @Autowired
    private ProjetRepository projetRepository;

    private ProjetRequest projetRequest;

    @BeforeEach
    void setUp() {

        projetRepository.deleteAll();

        projetRequest = new ProjetRequest();

        projetRequest.setNom("Projet Santé");
        projetRequest.setDescription("Projet médical");
        projetRequest.setZoneGeographique("Montréal");
        projetRequest.setDateDebut(LocalDate.now());
        projetRequest.setDateFin(LocalDate.now().plusDays(30));
        projetRequest.setType("Santé");
    }

    @Test
    void shouldCreateProject() {

        ProjetResponse savedProjet =
                projetService.creerProjet(projetRequest);

        assertNotNull(savedProjet);

        assertEquals("Projet Santé", savedProjet.getNom());

        assertEquals(1, projetRepository.count());
    }

    @Test
    void shouldReturnAllProjects() {

        projetService.creerProjet(projetRequest);

        List<ProjetResponse> projets =
                projetService.afficherTousLesProjets();

        assertEquals(1, projets.size());
    }

    @Test
    void shouldReturnProjectById() {

        ProjetResponse savedProjet =
                projetService.creerProjet(projetRequest);

        ProjetResponse foundProjet =
                projetService.afficherProjetParId(savedProjet.getId());

        assertNotNull(foundProjet);

        assertEquals("Projet Santé", foundProjet.getNom());
    }

    @Test
    void shouldUpdateProject() {

        ProjetResponse savedProjet =
                projetService.creerProjet(projetRequest);

        projetRequest.setNom("Projet Agriculture");

        ProjetResponse updatedProjet =
                projetService.modifierProjet(
                        savedProjet.getId(),
                        projetRequest
                );

        assertEquals(
                "Projet Agriculture",
                updatedProjet.getNom()
        );
    }

    @Test
    void shouldDeleteProject() {

        ProjetResponse savedProjet =
                projetService.creerProjet(projetRequest);

        projetService.supprimerProjet(savedProjet.getId());

        assertEquals(0, projetRepository.count());
    }
}

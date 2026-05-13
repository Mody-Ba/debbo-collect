package com.DebboCollect.DebboCollect;
import com.DebboCollect.DebboCollect.Model.ChampRequest;
import com.DebboCollect.DebboCollect.Model.ChampResponse;
import com.DebboCollect.DebboCollect.entity.Projet;
import com.DebboCollect.DebboCollect.repository.ChampRepository;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import com.DebboCollect.DebboCollect.services.ChampServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class ChampIntegrationTest {
    @Autowired
    private ChampServiceImp champService;

    @Autowired
    private ChampRepository champRepository;

    @Autowired
    private ProjetRepository projetRepository;

    private ChampRequest champRequest;

    private Projet projet;

    @BeforeEach
    void setUp() {

        champRepository.deleteAll();
        projetRepository.deleteAll();

        projet = new Projet();

        projet.setNom("Projet Santé");
        projet.setDescription("Description");
        projet.setZoneGeographique("Montréal");
        projet.setType("Santé");

        projet = projetRepository.save(projet);

        champRequest = new ChampRequest();

        champRequest.setType("Texte");
        champRequest.setQuestion("Quel est votre nom ?");
        champRequest.setPreuveObligatoire(true);
        champRequest.setProjetId(projet.getId());
    }

    @Test
    void shouldCreateChamp() {

        ChampResponse savedChamp =
                champService.creerChamp(champRequest);

        assertNotNull(savedChamp);

        assertEquals(
                "Quel est votre nom ?",
                savedChamp.getQuestion()
        );

        assertEquals(1, champRepository.count());
    }

    @Test
    void shouldReturnAllChamps() {

        champService.creerChamp(champRequest);

        List<ChampResponse> champs =
                champService.afficherTousLesChamps();

        assertEquals(1, champs.size());
    }

    @Test
    void shouldReturnChampById() {

        ChampResponse savedChamp =
                champService.creerChamp(champRequest);

        ChampResponse foundChamp =
                champService.afficherChampParId(savedChamp.getId());

        assertNotNull(foundChamp);

        assertEquals(
                "Quel est votre nom ?",
                foundChamp.getQuestion()
        );
    }

    @Test
    void shouldUpdateChamp() {

        ChampResponse savedChamp =
                champService.creerChamp(champRequest);

        champRequest.setQuestion("Quel est votre âge ?");

        ChampResponse updatedChamp =
                champService.modifierChamp(
                        savedChamp.getId(),
                        champRequest
                );

        assertEquals(
                "Quel est votre âge ?",
                updatedChamp.getQuestion()
        );
    }

    @Test
    void shouldDeleteChamp() {

        ChampResponse savedChamp =
                champService.creerChamp(champRequest);

        champService.supprimerChamp(savedChamp.getId());

        assertEquals(0, champRepository.count());
    }
}

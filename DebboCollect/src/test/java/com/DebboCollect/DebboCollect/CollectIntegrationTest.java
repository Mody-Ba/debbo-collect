package com.DebboCollect.DebboCollect;
import com.DebboCollect.DebboCollect.Model.CollectRequest;
import com.DebboCollect.DebboCollect.Model.CollectResponse;
import com.DebboCollect.DebboCollect.entity.Projet;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.entity.Role;
import com.DebboCollect.DebboCollect.repository.CollectRepository;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import com.DebboCollect.DebboCollect.services.CollectServiceImp;
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
public class CollectIntegrationTest {
    @Autowired
    private CollectServiceImp collectService;

    @Autowired
    private CollectRepository collectRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ProjetRepository projetRepository;

    private CollectRequest collectRequest;

    private Utilisateur utilisateur;

    private Projet projet;

    @BeforeEach
    void setUp() {

        collectRepository.deleteAll();
        utilisateurRepository.deleteAll();
        projetRepository.deleteAll();

        utilisateur = new Utilisateur();

        utilisateur.setNom("Mody");
        utilisateur.setEmail("mody@gmail.com");
        utilisateur.setMotDePasse("1234");
        utilisateur.setRole(Role.ADMIN);

        utilisateur = utilisateurRepository.save(utilisateur);

        projet = new Projet();

        projet.setNom("Projet Santé");
        projet.setDescription("Description");
        projet.setZoneGeographique("Montréal");
        projet.setType("Santé");

        projet = projetRepository.save(projet);

        collectRequest = new CollectRequest();

        collectRequest.setDateCollecte(LocalDate.now());
        collectRequest.setLocalisation("Québec");
        collectRequest.setEnqueteurId(utilisateur.getId());
        collectRequest.setProjetId(projet.getId());
    }

    @Test
    void shouldCreateCollecte() {

        CollectResponse savedCollecte =
                collectService.creerCollecte(collectRequest);

        assertNotNull(savedCollecte);

        assertEquals(
                "Québec",
                savedCollecte.getLocalisation()
        );

        assertEquals(1, collectRepository.count());
    }

    @Test
    void shouldReturnAllCollectes() {

        collectService.creerCollecte(collectRequest);

        List<CollectResponse> collectes =
                collectService.afficherToutesLesCollectes();

        assertEquals(1, collectes.size());
    }

    @Test
    void shouldReturnCollecteById() {

        CollectResponse savedCollecte =
                collectService.creerCollecte(collectRequest);

        CollectResponse foundCollecte =
                collectService.afficherCollecteParId(savedCollecte.getId());

        assertNotNull(foundCollecte);

        assertEquals(
                "Québec",
                foundCollecte.getLocalisation()
        );
    }

    @Test
    void shouldUpdateCollecte() {

        CollectResponse savedCollecte =
                collectService.creerCollecte(collectRequest);

        collectRequest.setLocalisation("Montréal");

        CollectResponse updatedCollecte =
                collectService.modifierCollecte(
                        savedCollecte.getId(),
                        collectRequest
                );

        assertEquals(
                "Montréal",
                updatedCollecte.getLocalisation()
        );
    }

    @Test
    void shouldDeleteCollecte() {

        CollectResponse savedCollecte =
                collectService.creerCollecte(collectRequest);

        collectService.supprimerCollecte(savedCollecte.getId());

        assertEquals(0, collectRepository.count());
    }
}

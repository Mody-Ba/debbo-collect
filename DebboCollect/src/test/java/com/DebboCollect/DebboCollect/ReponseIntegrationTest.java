package com.DebboCollect.DebboCollect;
import com.DebboCollect.DebboCollect.Model.ReponseRequest;
import com.DebboCollect.DebboCollect.Model.ReponseResponse;
import com.DebboCollect.DebboCollect.entity.Champ;
import com.DebboCollect.DebboCollect.entity.Collecte;
import com.DebboCollect.DebboCollect.entity.Projet;
import com.DebboCollect.DebboCollect.entity.Role;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.repository.ChampRepository;
import com.DebboCollect.DebboCollect.repository.CollectRepository;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import com.DebboCollect.DebboCollect.repository.ReponseRepository;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import com.DebboCollect.DebboCollect.services.ReponseServiceImp;
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

public class ReponseIntegrationTest {
    @Autowired
    private ReponseServiceImp reponseService;

    @Autowired
    private ReponseRepository reponseRepository;

    @Autowired
    private ChampRepository champRepository;

    @Autowired
    private CollectRepository collectRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private ReponseRequest reponseRequest;

    private Champ champ;

    private Collecte collecte;

    @BeforeEach
    void setUp() {

        reponseRepository.deleteAll();
        champRepository.deleteAll();
        collectRepository.deleteAll();
        projetRepository.deleteAll();
        utilisateurRepository.deleteAll();

        Projet projet = new Projet();
        projet.setNom("Projet Santé");
        projet.setDescription("Description");
        projet.setZoneGeographique("Montréal");
        projet.setType("Santé");
        projet = projetRepository.save(projet);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom("Mody");
        utilisateur.setEmail("mody@gmail.com");
        utilisateur.setMotDePasse("1234");
        utilisateur.setRole(Role.ADMIN);
        utilisateur = utilisateurRepository.save(utilisateur);

        champ = new Champ();
        champ.setType("Texte");
        champ.setQuestion("Quel est votre nom ?");
        champ.setPreuveObligatoire(true);
        champ.setProjet(projet);
        champ = champRepository.save(champ);

        collecte = new Collecte();
        collecte.setDateCollecte(LocalDate.now());
        collecte.setLocalisation("Québec");
        collecte.setEnqueteur(utilisateur);
        collecte.setProjet(projet);
        collecte = collectRepository.save(collecte);

        reponseRequest = new ReponseRequest();
        reponseRequest.setValeur("Oui");
        reponseRequest.setChampId(champ.getId());
        reponseRequest.setCollecteId(collecte.getId());
    }

    @Test
    void shouldCreateReponse() {

        ReponseResponse savedReponse =
                reponseService.creerReponse(reponseRequest);

        assertNotNull(savedReponse);
        assertEquals("Oui", savedReponse.getValeur());
        assertEquals(1, reponseRepository.count());
    }

    @Test
    void shouldReturnAllReponses() {

        reponseService.creerReponse(reponseRequest);

        List<ReponseResponse> reponses =
                reponseService.afficherToutesLesReponses();

        assertEquals(1, reponses.size());
    }

    @Test
    void shouldReturnReponseById() {

        ReponseResponse savedReponse =
                reponseService.creerReponse(reponseRequest);

        ReponseResponse foundReponse =
                reponseService.afficherReponseParId(savedReponse.getId());

        assertNotNull(foundReponse);
        assertEquals("Oui", foundReponse.getValeur());
    }

    @Test
    void shouldUpdateReponse() {

        ReponseResponse savedReponse =
                reponseService.creerReponse(reponseRequest);

        reponseRequest.setValeur("Non");

        ReponseResponse updatedReponse =
                reponseService.modifierReponse(
                        savedReponse.getId(),
                        reponseRequest
                );

        assertEquals("Non", updatedReponse.getValeur());
    }

    @Test
    void shouldDeleteReponse() {

        ReponseResponse savedReponse =
                reponseService.creerReponse(reponseRequest);

        reponseService.supprimerReponse(savedReponse.getId());

        assertEquals(0, reponseRepository.count());
    }
}

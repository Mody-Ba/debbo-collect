package com.DebboCollect.DebboCollect;
import com.DebboCollect.DebboCollect.Model.MediaRequest;
import com.DebboCollect.DebboCollect.Model.MediaResponse;
import com.DebboCollect.DebboCollect.entity.*;
import com.DebboCollect.DebboCollect.repository.*;
import com.DebboCollect.DebboCollect.services.MediaServiceImp;
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
public class MediaIntegrationTest {
    @Autowired
    private MediaServiceImp mediaService;

    @Autowired
    private MediaRepository mediaRepository;

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

    private MediaRequest mediaRequest;

    private Reponse reponse;

    @BeforeEach
    void setUp() {

        mediaRepository.deleteAll();
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

        Champ champ = new Champ();

        champ.setType("Texte");
        champ.setQuestion("Quel est votre nom ?");
        champ.setPreuveObligatoire(true);
        champ.setProjet(projet);

        champ = champRepository.save(champ);

        Collecte collecte = new Collecte();

        collecte.setDateCollecte(LocalDate.now());
        collecte.setLocalisation("Québec");
        collecte.setEnqueteur(utilisateur);
        collecte.setProjet(projet);

        collecte = collectRepository.save(collecte);

        reponse = new Reponse();

        reponse.setValeur("Oui");
        reponse.setChamp(champ);
        reponse.setCollecte(collecte);

        reponse = reponseRepository.save(reponse);

        mediaRequest = new MediaRequest();

        mediaRequest.setType("Image");
        mediaRequest.setUrl("photo.jpg");
        mediaRequest.setReponseId(reponse.getId());
    }

    @Test
    void shouldCreateMedia() {

        MediaResponse savedMedia =
                mediaService.creerMedia(mediaRequest);

        assertNotNull(savedMedia);

        assertEquals(
                "Image",
                savedMedia.getType()
        );

        assertEquals(1, mediaRepository.count());
    }

    @Test
    void shouldReturnAllMedias() {

        mediaService.creerMedia(mediaRequest);

        List<MediaResponse> medias =
                mediaService.afficherTousLesMedias();

        assertEquals(1, medias.size());
    }

    @Test
    void shouldReturnMediaById() {

        MediaResponse savedMedia =
                mediaService.creerMedia(mediaRequest);

        MediaResponse foundMedia =
                mediaService.afficherMediaParId(savedMedia.getId());

        assertNotNull(foundMedia);

        assertEquals(
                "Image",
                foundMedia.getType()
        );
    }

    @Test
    void shouldUpdateMedia() {

        MediaResponse savedMedia =
                mediaService.creerMedia(mediaRequest);

        mediaRequest.setType("Audio");

        MediaResponse updatedMedia =
                mediaService.modifierMedia(
                        savedMedia.getId(),
                        mediaRequest
                );

        assertEquals(
                "Audio",
                updatedMedia.getType()
        );
    }

    @Test
    void shouldDeleteMedia() {

        MediaResponse savedMedia =
                mediaService.creerMedia(mediaRequest);

        mediaService.supprimerMedia(savedMedia.getId());

        assertEquals(0, mediaRepository.count());
    }


}

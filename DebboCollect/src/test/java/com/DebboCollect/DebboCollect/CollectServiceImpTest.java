package com.DebboCollect.DebboCollect;
import com.DebboCollect.DebboCollect.Model.CollectRequest;
import com.DebboCollect.DebboCollect.Model.CollectResponse;
import com.DebboCollect.DebboCollect.entity.Collecte;
import com.DebboCollect.DebboCollect.entity.Projet;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.mappers.CollectMapper;
import com.DebboCollect.DebboCollect.repository.CollectRepository;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import com.DebboCollect.DebboCollect.services.CollectServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CollectServiceImpTest {

    @Mock
    private CollectRepository collectRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private ProjetRepository projetRepository;

    @Mock
    private CollectMapper collectMapper;

    @InjectMocks
    private CollectServiceImp collectService;

    private Collecte collecte;
    private CollectRequest collectRequest;
    private CollectResponse collectResponse;
    private Utilisateur utilisateur;
    private Projet projet;

    @BeforeEach
    void setUp() {

        utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        projet = new Projet();
        projet.setId(1L);

        collecte = new Collecte();
        collecte.setId(1L);
        collecte.setDateCollecte(LocalDate.now());
        collecte.setLocalisation("Canada");
        collecte.setEnqueteur(utilisateur);
        collecte.setProjet(projet);

        collectRequest = new CollectRequest();
        collectRequest.setDateCollecte(LocalDate.now());
        collectRequest.setLocalisation("Canada");
        collectRequest.setEnqueteurId(1L);
        collectRequest.setProjetId(1L);

        collectResponse = new CollectResponse();
        collectResponse.setId(1L);
        collectResponse.setDateCollecte(LocalDate.now());
        collectResponse.setLocalisation("Canada");
    }

    @Test
    void shouldCreateCollecte() {

        when(utilisateurRepository.findById(1L))
                .thenReturn(Optional.of(utilisateur));

        when(projetRepository.findById(1L))
                .thenReturn(Optional.of(projet));

        when(collectMapper.toEntity(collectRequest, utilisateur, projet))
                .thenReturn(collecte);

        when(collectRepository.save(collecte))
                .thenReturn(collecte);

        when(collectMapper.toResponse(collecte))
                .thenReturn(collectResponse);

        CollectResponse result =
                collectService.creerCollecte(collectRequest);

        assertEquals("Canada", result.getLocalisation());
    }

    @Test
    void shouldReturnAllCollectes() {

        when(collectRepository.findAll())
                .thenReturn(List.of(collecte));

        when(collectMapper.toResponse(collecte))
                .thenReturn(collectResponse);

        List<CollectResponse> result =
                collectService.afficherToutesLesCollectes();

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnCollecteById() {

        when(collectRepository.findById(1L))
                .thenReturn(Optional.of(collecte));

        when(collectMapper.toResponse(collecte))
                .thenReturn(collectResponse);

        CollectResponse result =
                collectService.afficherCollecteParId(1L);

        assertEquals("Canada", result.getLocalisation());
    }

    @Test
    void shouldUpdateCollecte() {

        when(collectRepository.findById(1L))
                .thenReturn(Optional.of(collecte));

        when(utilisateurRepository.findById(1L))
                .thenReturn(Optional.of(utilisateur));

        when(projetRepository.findById(1L))
                .thenReturn(Optional.of(projet));

        when(collectRepository.save(collecte))
                .thenReturn(collecte);

        when(collectMapper.toResponse(collecte))
                .thenReturn(collectResponse);

        CollectResponse result =
                collectService.modifierCollecte(1L, collectRequest);

        assertEquals("Canada", result.getLocalisation());
    }

    @Test
    void shouldDeleteCollecte() {

        doNothing().when(collectRepository)
                .deleteById(1L);

        collectService.supprimerCollecte(1L);

        verify(collectRepository, times(1))
                .deleteById(1L);
    }
}

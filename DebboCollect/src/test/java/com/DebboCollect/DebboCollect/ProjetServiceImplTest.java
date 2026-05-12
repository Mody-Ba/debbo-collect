package com.DebboCollect.DebboCollect;
import com.DebboCollect.DebboCollect.Model.ProjetRequest;
import com.DebboCollect.DebboCollect.Model.ProjetResponse;
import com.DebboCollect.DebboCollect.entity.Projet;
import com.DebboCollect.DebboCollect.mappers.ProjetMapper;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import com.DebboCollect.DebboCollect.services.ProjetServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ProjetServiceImplTest {
    @Mock
    private ProjetRepository projetRepository;

    @Mock
    private ProjetMapper projetMapper;

    @InjectMocks
    private ProjetServiceImpl projetService;

    private Projet projet;
    private ProjetRequest projetRequest;
    private ProjetResponse projetResponse;

    @BeforeEach
    void setUp() {

        projet = new Projet();

        projet.setId(1L);
        projet.setNom("Projet Santé");
        projet.setDescription("Description");
        projet.setZoneGeographique("Canada");
        projet.setDateDebut(LocalDate.now());
        projet.setDateFin(LocalDate.now().plusDays(30));
        projet.setType("Enquête");

        projetRequest = new ProjetRequest();

        projetRequest.setNom("Projet Santé");
        projetRequest.setDescription("Description");
        projetRequest.setZoneGeographique("Canada");
        projetRequest.setDateDebut(LocalDate.now());
        projetRequest.setDateFin(LocalDate.now().plusDays(30));
        projetRequest.setType("Enquête");

        projetResponse = new ProjetResponse();

        projetResponse.setId(1L);
        projetResponse.setNom("Projet Santé");
        projetResponse.setDescription("Description");
        projetResponse.setZoneGeographique("Canada");
        projetResponse.setDateDebut(LocalDate.now());
        projetResponse.setDateFin(LocalDate.now().plusDays(30));
        projetResponse.setType("Enquête");
    }

    @Test
    void shouldCreateProjet() {

        when(projetMapper.toEntity(projetRequest))
                .thenReturn(projet);

        when(projetRepository.save(projet))
                .thenReturn(projet);

        when(projetMapper.toResponse(projet))
                .thenReturn(projetResponse);

        ProjetResponse result =
                projetService.creerProjet(projetRequest);

        assertEquals("Projet Santé", result.getNom());
    }

    @Test
    void shouldReturnAllProjects() {

        when(projetRepository.findAll())
                .thenReturn(List.of(projet));

        when(projetMapper.toResponse(projet))
                .thenReturn(projetResponse);

        List<ProjetResponse> result =
                projetService.afficherTousLesProjets();

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnProjectById() {

        when(projetRepository.findById(1L))
                .thenReturn(Optional.of(projet));

        when(projetMapper.toResponse(projet))
                .thenReturn(projetResponse);

        ProjetResponse result =
                projetService.afficherProjetParId(1L);

        assertEquals("Projet Santé", result.getNom());
    }

    @Test
    void shouldUpdateProject() {

        when(projetRepository.findById(1L))
                .thenReturn(Optional.of(projet));

        doNothing().when(projetMapper)
                .updateEntityFromRequest(projetRequest, projet);

        when(projetRepository.save(projet))
                .thenReturn(projet);

        when(projetMapper.toResponse(projet))
                .thenReturn(projetResponse);

        ProjetResponse result =
                projetService.modifierProjet(1L, projetRequest);

        assertEquals("Projet Santé", result.getNom());
    }

    @Test
    void shouldDeleteProject() {

        doNothing().when(projetRepository)
                .deleteById(1L);

        projetService.supprimerProjet(1L);

        verify(projetRepository, times(1))
                .deleteById(1L);
    }
}

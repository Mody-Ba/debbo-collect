package com.DebboCollect.DebboCollect;
import com.DebboCollect.DebboCollect.Model.ChampRequest;
import com.DebboCollect.DebboCollect.Model.ChampResponse;
import com.DebboCollect.DebboCollect.entity.Champ;
import com.DebboCollect.DebboCollect.entity.Projet;
import com.DebboCollect.DebboCollect.mappers.ChampMapper;
import com.DebboCollect.DebboCollect.repository.ChampRepository;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import com.DebboCollect.DebboCollect.services.ChampServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChampServiceImplTest {
    @Mock
    private ChampRepository champRepository;

    @Mock
    private ProjetRepository projetRepository;

    @Mock
    private ChampMapper champMapper;

    @InjectMocks
    private ChampServiceImp champService;

    private Champ champ;
    private ChampRequest champRequest;
    private ChampResponse champResponse;
    private Projet projet;

    @BeforeEach
    void setUp() {

        projet = new Projet();
        projet.setId(1L);

        champ = new Champ();
        champ.setId(1L);
        champ.setType("Texte");
        champ.setQuestion("Quel est votre nom ?");
        champ.setPreuveObligatoire(true);
        champ.setProjet(projet);

        champRequest = new ChampRequest();
        champRequest.setType("Texte");
        champRequest.setQuestion("Quel est votre nom ?");
        champRequest.setPreuveObligatoire(true);
        champRequest.setProjetId(1L);

        champResponse = new ChampResponse();
        champResponse.setId(1L);
        champResponse.setType("Texte");
        champResponse.setQuestion("Quel est votre nom ?");
        champResponse.setPreuveObligatoire(true);
    }

    @Test
    void shouldCreateChamp() {

        when(projetRepository.findById(1L))
                .thenReturn(Optional.of(projet));

        when(champMapper.toEntity(champRequest, projet))
                .thenReturn(champ);

        when(champRepository.save(champ))
                .thenReturn(champ);

        when(champMapper.toResponse(champ))
                .thenReturn(champResponse);

        ChampResponse result =
                champService.creerChamp(champRequest);

        assertEquals("Texte", result.getType());
    }

    @Test
    void shouldReturnAllChamps() {

        when(champRepository.findAll())
                .thenReturn(List.of(champ));

        when(champMapper.toResponse(champ))
                .thenReturn(champResponse);

        List<ChampResponse> result =
                champService.afficherTousLesChamps();

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnChampById() {

        when(champRepository.findById(1L))
                .thenReturn(Optional.of(champ));

        when(champMapper.toResponse(champ))
                .thenReturn(champResponse);

        ChampResponse result =
                champService.afficherChampParId(1L);

        assertEquals("Texte", result.getType());
    }

    @Test
    void shouldUpdateChamp() {

        when(champRepository.findById(1L))
                .thenReturn(Optional.of(champ));

        when(projetRepository.findById(1L))
                .thenReturn(Optional.of(projet));

        when(champRepository.save(champ))
                .thenReturn(champ);

        when(champMapper.toResponse(champ))
                .thenReturn(champResponse);

        ChampResponse result =
                champService.modifierChamp(1L, champRequest);

        assertEquals("Texte", result.getType());
    }

    @Test
    void shouldDeleteChamp() {

        doNothing().when(champRepository)
                .deleteById(1L);

        champService.supprimerChamp(1L);

        verify(champRepository, times(1))
                .deleteById(1L);
    }
}

package com.DebboCollect.DebboCollect;
import com.DebboCollect.DebboCollect.Model.ReponseRequest;
import com.DebboCollect.DebboCollect.Model.ReponseResponse;
import com.DebboCollect.DebboCollect.entity.Champ;
import com.DebboCollect.DebboCollect.entity.Collecte;
import com.DebboCollect.DebboCollect.entity.Reponse;
import com.DebboCollect.DebboCollect.mappers.ReponseMapper;
import com.DebboCollect.DebboCollect.repository.ChampRepository;
import com.DebboCollect.DebboCollect.repository.CollectRepository;
import com.DebboCollect.DebboCollect.repository.ReponseRepository;
import com.DebboCollect.DebboCollect.services.ReponseServiceImp;
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
public class ReponseServiceImpTest {
    @Mock
    private ReponseRepository reponseRepository;

    @Mock
    private ChampRepository champRepository;

    @Mock
    private CollectRepository collecteRepository;

    @Mock
    private ReponseMapper reponseMapper;

    @InjectMocks
    private ReponseServiceImp reponseService;

    private Reponse reponse;
    private ReponseRequest reponseRequest;
    private ReponseResponse reponseResponse;
    private Champ champ;
    private Collecte collecte;

    @BeforeEach
    void setUp() {

        champ = new Champ();
        champ.setId(1L);

        collecte = new Collecte();
        collecte.setId(1L);

        reponse = new Reponse();
        reponse.setId(1L);
        reponse.setValeur("Oui");
        reponse.setChamp(champ);
        reponse.setCollecte(collecte);

        reponseRequest = new ReponseRequest();
        reponseRequest.setValeur("Oui");
        reponseRequest.setChampId(1L);
        reponseRequest.setCollecteId(1L);

        reponseResponse = new ReponseResponse();
        reponseResponse.setId(1L);
        reponseResponse.setValeur("Oui");
    }

    @Test
    void shouldCreateReponse() {

        when(champRepository.findById(1L))
                .thenReturn(Optional.of(champ));

        when(collecteRepository.findById(1L))
                .thenReturn(Optional.of(collecte));

        when(reponseMapper.toEntity(reponseRequest, champ, collecte))
                .thenReturn(reponse);

        when(reponseRepository.save(reponse))
                .thenReturn(reponse);

        when(reponseMapper.toResponse(reponse))
                .thenReturn(reponseResponse);

        ReponseResponse result =
                reponseService.creerReponse(reponseRequest);

        assertEquals("Oui", result.getValeur());
    }

    @Test
    void shouldReturnAllReponses() {

        when(reponseRepository.findAll())
                .thenReturn(List.of(reponse));

        when(reponseMapper.toResponse(reponse))
                .thenReturn(reponseResponse);

        List<ReponseResponse> result =
                reponseService.afficherToutesLesReponses();

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnReponseById() {

        when(reponseRepository.findById(1L))
                .thenReturn(Optional.of(reponse));

        when(reponseMapper.toResponse(reponse))
                .thenReturn(reponseResponse);

        ReponseResponse result =
                reponseService.afficherReponseParId(1L);

        assertEquals("Oui", result.getValeur());
    }

    @Test
    void shouldUpdateReponse() {

        when(reponseRepository.findById(1L))
                .thenReturn(Optional.of(reponse));

        when(champRepository.findById(1L))
                .thenReturn(Optional.of(champ));

        when(collecteRepository.findById(1L))
                .thenReturn(Optional.of(collecte));

        when(reponseRepository.save(reponse))
                .thenReturn(reponse);

        when(reponseMapper.toResponse(reponse))
                .thenReturn(reponseResponse);

        ReponseResponse result =
                reponseService.modifierReponse(1L, reponseRequest);

        assertEquals("Oui", result.getValeur());
    }

    @Test
    void shouldDeleteReponse() {

        doNothing().when(reponseRepository)
                .deleteById(1L);

        reponseService.supprimerReponse(1L);

        verify(reponseRepository, times(1))
                .deleteById(1L);
    }

}

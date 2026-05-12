package com.DebboCollect.DebboCollect;
import com.DebboCollect.DebboCollect.Model.MediaRequest;
import com.DebboCollect.DebboCollect.Model.MediaResponse;
import com.DebboCollect.DebboCollect.entity.Media;
import com.DebboCollect.DebboCollect.entity.Reponse;
import com.DebboCollect.DebboCollect.mappers.MediaMapper;
import com.DebboCollect.DebboCollect.repository.MediaRepository;
import com.DebboCollect.DebboCollect.repository.ReponseRepository;
import com.DebboCollect.DebboCollect.services.MediaServiceImp;
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
public class MediaServiceImplTest {
    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private ReponseRepository reponseRepository;

    @Mock
    private MediaMapper mediaMapper;

    @InjectMocks
    private MediaServiceImp mediaService;

    private Media media;
    private MediaRequest mediaRequest;
    private MediaResponse mediaResponse;
    private Reponse reponse;

    @BeforeEach
    void setUp() {

        reponse = new Reponse();
        reponse.setId(1L);

        media = new Media();
        media.setId(1L);
        media.setType("Audio");
        media.setUrl("audio.mp3");
        media.setReponse(reponse);

        mediaRequest = new MediaRequest();
        mediaRequest.setType("Audio");
        mediaRequest.setUrl("audio.mp3");
        mediaRequest.setReponseId(1L);

        mediaResponse = new MediaResponse();
        mediaResponse.setId(1L);
        mediaResponse.setType("Audio");
        mediaResponse.setUrl("audio.mp3");
    }

    @Test
    void shouldCreateMedia() {

        when(reponseRepository.findById(1L))
                .thenReturn(Optional.of(reponse));

        when(mediaMapper.toEntity(mediaRequest, reponse))
                .thenReturn(media);

        when(mediaRepository.save(media))
                .thenReturn(media);

        when(mediaMapper.toResponse(media))
                .thenReturn(mediaResponse);

        MediaResponse result =
                mediaService.creerMedia(mediaRequest);

        assertEquals("Audio", result.getType());
    }

    @Test
    void shouldReturnAllMedias() {

        when(mediaRepository.findAll())
                .thenReturn(List.of(media));

        when(mediaMapper.toResponse(media))
                .thenReturn(mediaResponse);

        List<MediaResponse> result =
                mediaService.afficherTousLesMedias();

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnMediaById() {

        when(mediaRepository.findById(1L))
                .thenReturn(Optional.of(media));

        when(mediaMapper.toResponse(media))
                .thenReturn(mediaResponse);

        MediaResponse result =
                mediaService.afficherMediaParId(1L);

        assertEquals("Audio", result.getType());
    }

    @Test
    void shouldUpdateMedia() {

        when(mediaRepository.findById(1L))
                .thenReturn(Optional.of(media));

        when(reponseRepository.findById(1L))
                .thenReturn(Optional.of(reponse));

        when(mediaRepository.save(media))
                .thenReturn(media);

        when(mediaMapper.toResponse(media))
                .thenReturn(mediaResponse);

        MediaResponse result =
                mediaService.modifierMedia(1L, mediaRequest);

        assertEquals("Audio", result.getType());
    }

    @Test
    void shouldDeleteMedia() {

        doNothing().when(mediaRepository)
                .deleteById(1L);

        mediaService.supprimerMedia(1L);

        verify(mediaRepository, times(1))
                .deleteById(1L);
    }
}

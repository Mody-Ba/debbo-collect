package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.MediaRequest;
import com.DebboCollect.DebboCollect.Model.MediaResponse;
import com.DebboCollect.DebboCollect.entity.Media;
import com.DebboCollect.DebboCollect.entity.Reponse;
import com.DebboCollect.DebboCollect.mappers.MediaMapper;
import com.DebboCollect.DebboCollect.repository.MediaRepository;
import com.DebboCollect.DebboCollect.repository.ReponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaServiceImp implements MediaService {

    private final MediaRepository mediaRepository;
    private final ReponseRepository reponseRepository;
    private final MediaMapper mediaMapper;

    @Override
    public MediaResponse creerMedia(MediaRequest request) {

        Reponse reponse = reponseRepository.findById(request.getReponseId())
                .orElseThrow(() -> new RuntimeException("Réponse non trouvée"));

        Media media = mediaMapper.toEntity(request, reponse);

        Media savedMedia = mediaRepository.save(media);

        return mediaMapper.toResponse(savedMedia);
    }

    @Override
    public List<MediaResponse> afficherTousLesMedias() {

        return mediaRepository.findAll()
                .stream()
                .map(mediaMapper::toResponse)
                .toList();
    }

    @Override
    public MediaResponse afficherMediaParId(Long id) {

        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media non trouvé"));

        return mediaMapper.toResponse(media);
    }

    @Override
    public MediaResponse modifierMedia(Long id, MediaRequest request) {

        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media non trouvé"));

        Reponse reponse = reponseRepository.findById(request.getReponseId())
                .orElseThrow(() -> new RuntimeException("Réponse non trouvée"));

        media.setType(request.getType());
        media.setUrl(request.getUrl());
        media.setReponse(reponse);

        Media updatedMedia = mediaRepository.save(media);

        return mediaMapper.toResponse(updatedMedia);
    }

    @Override
    public void supprimerMedia(Long id) {

        mediaRepository.deleteById(id);
    }
}
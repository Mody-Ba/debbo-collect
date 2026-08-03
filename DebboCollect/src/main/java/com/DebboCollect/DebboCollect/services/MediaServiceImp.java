package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.MediaRequest;
import com.DebboCollect.DebboCollect.Model.MediaResponse;
import com.DebboCollect.DebboCollect.entity.Media;
import com.DebboCollect.DebboCollect.entity.Reponse;
import com.DebboCollect.DebboCollect.entity.StatusCollect;
import com.DebboCollect.DebboCollect.mappers.MediaMapper;
import com.DebboCollect.DebboCollect.repository.MediaRepository;
import com.DebboCollect.DebboCollect.repository.ReponseRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MediaServiceImp implements MediaService {

    private final MediaRepository mediaRepository;
    private final ReponseRepository reponseRepository;
    private final MediaMapper mediaMapper;
    private final Cloudinary cloudinary;

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
    public List<MediaResponse> getMediaByReponse(Long reponseId) {

        return mediaRepository.findByReponseId(reponseId)
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

        if (media.getReponse()
                .getCollecte()
                .getStatut() == StatusCollect.VALIDEE) {

            throw new RuntimeException("Impossible de modifier un média d'une collecte validée");
        }

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

        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media non trouvé"));

        if (media.getReponse()
                .getCollecte()
                .getStatut() == StatusCollect.VALIDEE) {

            throw new RuntimeException("Impossible de supprimer un média d'une collecte validée");
        }

        mediaRepository.delete(media);
    }

    @Override
    public String uploadFile(MultipartFile file) {

        try {

            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "folder", "debbocollect"
                    )
            );

            return (String) uploadResult.get("secure_url");

        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("Erreur upload fichier", e);
        }
    }
}
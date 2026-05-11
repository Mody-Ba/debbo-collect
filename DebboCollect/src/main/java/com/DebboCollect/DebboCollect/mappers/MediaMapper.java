package com.DebboCollect.DebboCollect.mappers;

import com.DebboCollect.DebboCollect.Model.MediaModel;
import com.DebboCollect.DebboCollect.Model.MediaRequest;
import com.DebboCollect.DebboCollect.Model.MediaResponse;
import com.DebboCollect.DebboCollect.entity.Media;
import com.DebboCollect.DebboCollect.entity.Reponse;
import org.springframework.stereotype.Component;

@Component
public class MediaMapper {

    public Media toEntity(MediaRequest request, Reponse reponse) {

        return Media.builder()
                .type(request.getType())
                .url(request.getUrl())
                .reponse(reponse)
                .build();
    }

    public MediaResponse toResponse(Media media) {

        return MediaResponse.builder()
                .id(media.getId())
                .type(media.getType())
                .url(media.getUrl())
                .reponseId(media.getReponse().getId())
                .build();
    }

    public MediaModel toModel(Media media) {

        return MediaModel.builder()
                .id(media.getId())
                .type(media.getType())
                .url(media.getUrl())
                .reponseId(media.getReponse().getId())
                .build();
    }
}
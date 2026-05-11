package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.MediaRequest;
import com.DebboCollect.DebboCollect.Model.MediaResponse;

import java.util.List;

public interface MediaService {

    MediaResponse creerMedia(MediaRequest request);

    List<MediaResponse> afficherTousLesMedias();

    MediaResponse afficherMediaParId(Long id);

    MediaResponse modifierMedia(Long id, MediaRequest request);

    void supprimerMedia(Long id);
}
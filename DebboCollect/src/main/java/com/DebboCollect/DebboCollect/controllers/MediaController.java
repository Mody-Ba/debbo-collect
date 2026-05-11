package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.MediaRequest;
import com.DebboCollect.DebboCollect.Model.MediaResponse;
import com.DebboCollect.DebboCollect.services.MediaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medias")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping
    public MediaResponse creerMedia(@Valid @RequestBody MediaRequest request) {

        return mediaService.creerMedia(request);
    }

    @GetMapping
    public List<MediaResponse> afficherTousLesMedias() {

        return mediaService.afficherTousLesMedias();
    }

    @GetMapping("/{id}")
    public MediaResponse afficherMediaParId(@PathVariable Long id) {

        return mediaService.afficherMediaParId(id);
    }

    @PutMapping("/{id}")
    public MediaResponse modifierMedia(@PathVariable Long id,
                                       @RequestBody MediaRequest request) {

        return mediaService.modifierMedia(id, request);
    }

    @DeleteMapping("/{id}")
    public void supprimerMedia(Long id) {

        mediaService.supprimerMedia(id);
    }
}
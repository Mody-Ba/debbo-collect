package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.MediaRequest;
import com.DebboCollect.DebboCollect.Model.MediaResponse;
import com.DebboCollect.DebboCollect.services.MediaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/medias")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping
    @PreAuthorize("hasRole('ENQUETEUR')")
    public MediaResponse creerMedia(@Valid @RequestBody MediaRequest request) {

        return mediaService.creerMedia(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPERVISEUR')")
    public List<MediaResponse> afficherTousLesMedias() {

        return mediaService.afficherTousLesMedias();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPERVISEUR')")
    public MediaResponse afficherMediaParId(@PathVariable Long id) {

        return mediaService.afficherMediaParId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ENQUETEUR')")
    public MediaResponse modifierMedia(@PathVariable Long id,
                                       @RequestBody MediaRequest request) {

        return mediaService.modifierMedia(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ENQUETEUR')")
    public void supprimerMedia(Long id) {

        mediaService.supprimerMedia(id);
    }
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadMedia(@RequestParam("file") MultipartFile file) {

        return mediaService.uploadFile(file);
    }
}
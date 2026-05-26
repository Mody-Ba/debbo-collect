package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.UtilisateurModel;
import com.DebboCollect.DebboCollect.Model.UtilisateurRequest;
import com.DebboCollect.DebboCollect.Model.UtilisateurResponse;
import com.DebboCollect.DebboCollect.mappers.UtilisateurMapper;
import com.DebboCollect.DebboCollect.services.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/superviseurs")
    @PreAuthorize("hasRole('ADMIN')")
    public UtilisateurResponse createSuperviseur(
            @Valid @RequestBody UtilisateurRequest request
    ) {

        request.setRole(com.DebboCollect.DebboCollect.entity.Role.SUPERVISEUR
        );

        request.setCompteActif(false);

        UtilisateurModel model = UtilisateurMapper.toModel(request);

        model = utilisateurService.createUser(model);

        return UtilisateurMapper.toResponse(model);
    }

    @PostMapping("/bailleurs")
    @PreAuthorize("hasRole('SUPERVISEUR')")
    public UtilisateurResponse createBailleur(
            @Valid @RequestBody UtilisateurRequest request
    ) {

        request.setRole(
                com.DebboCollect.DebboCollect.entity.Role.BAILLEUR
        );

        request.setCompteActif(true);

        UtilisateurModel model =
                UtilisateurMapper.toModel(request);

        model = utilisateurService.createUser(model);

        return UtilisateurMapper.toResponse(model);
    }

    @PostMapping("/enqueteurs")
    @PreAuthorize("hasRole('SUPERVISEUR')")
    public UtilisateurResponse createEnqueteur(
            @Valid @RequestBody UtilisateurRequest request
    ) {

        request.setRole(
                com.DebboCollect.DebboCollect.entity.Role.ENQUETEUR
        );

        request.setCompteActif(false);

        UtilisateurModel model =
                UtilisateurMapper.toModel(request);

        model = utilisateurService.createUser(model);

        return UtilisateurMapper.toResponse(model);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR')")
    public List<UtilisateurResponse> getAllUsers() {

        return utilisateurService.getAllUsers()
                .stream()
                .map(UtilisateurMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR')")
    public UtilisateurResponse getUserById(@PathVariable Long id) {

        UtilisateurModel model = utilisateurService.getUserById(id);

        return UtilisateurMapper.toResponse(model);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR')")
    public UtilisateurResponse updateUser(@PathVariable Long id,
                                          @RequestBody UtilisateurRequest request) {

        UtilisateurModel model = UtilisateurMapper.toModel(request);

        model = utilisateurService.updateUser(id, model);

        return UtilisateurMapper.toResponse(model);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR')")
    public void deleteUser(@PathVariable Long id) {

        utilisateurService.deleteUser(id);
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR')")
    public UtilisateurResponse activateUser(@PathVariable Long id) {

        UtilisateurModel model = utilisateurService.activateUser(id);

        return UtilisateurMapper.toResponse(model);
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISEUR')")
    public UtilisateurResponse deactivateUser(@PathVariable Long id) {

        UtilisateurModel model = utilisateurService.deactivateUser(id);

        return UtilisateurMapper.toResponse(model);
    }
}

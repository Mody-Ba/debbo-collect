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

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UtilisateurResponse createUser(@Valid @RequestBody UtilisateurRequest request) {

        UtilisateurModel model = UtilisateurMapper.toModel(request);

        model = utilisateurService.createUser(model);

        return UtilisateurMapper.toResponse(model);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UtilisateurResponse> getAllUsers() {

        return utilisateurService.getAllUsers()
                .stream()
                .map(UtilisateurMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UtilisateurResponse getUserById(@PathVariable Long id) {

        UtilisateurModel model = utilisateurService.getUserById(id);

        return UtilisateurMapper.toResponse(model);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UtilisateurResponse updateUser(@PathVariable Long id,
                                          @RequestBody UtilisateurRequest request) {

        UtilisateurModel model = UtilisateurMapper.toModel(request);

        model = utilisateurService.updateUser(id, model);

        return UtilisateurMapper.toResponse(model);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {

        utilisateurService.deleteUser(id);
    }
}

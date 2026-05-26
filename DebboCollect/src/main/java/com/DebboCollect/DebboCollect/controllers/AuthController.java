package com.DebboCollect.DebboCollect.controllers;

import com.DebboCollect.DebboCollect.Model.JwtResponse;
import com.DebboCollect.DebboCollect.Model.LoginRequest;
import com.DebboCollect.DebboCollect.Model.RegisterRequest;
import com.DebboCollect.DebboCollect.entity.Role;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import com.DebboCollect.DebboCollect.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;



    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(

            @RequestBody
            LoginRequest request
    ) {

        return ResponseEntity.ok(

                authService.login(request)
        );
    }

    @PostMapping("/create-admin")
    public String createAdmin() {

        Utilisateur admin = new Utilisateur();

        admin.setNom("Admin");

        admin.setEmail("admin@gmail.com");

        admin.setPassword(passwordEncoder.encode("password"));

        admin.setRole(Role.ADMIN);

        admin.setCompteActif(true);

        utilisateurRepository.save(admin);

        return "Admin créé";
    }
}
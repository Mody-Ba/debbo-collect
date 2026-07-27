package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.JwtResponse;
import com.DebboCollect.DebboCollect.Model.LoginRequest;
import com.DebboCollect.DebboCollect.Model.RegisterRequest;
import com.DebboCollect.DebboCollect.configs.JwtUtils;
import com.DebboCollect.DebboCollect.entity.Role;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.exception.CustomResponseException;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final UtilisateurRepository utilisateurRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    public JwtResponse login(LoginRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        String password = request.getPassword();

        Utilisateur utilisateur = utilisateurRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new CustomResponseException(
                                "Utilisateur introuvable",
                                404
                        )
                );

        if (!utilisateur.getCompteActif()) {

            String message =
                    utilisateur.getRole() == Role.SUPERVISEUR
                            ? "Compte désactivé par l'administrateur"
                            : "Compte désactivé par le superviseur";

            throw new CustomResponseException(message, 403);
        }

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            password
                    )
            );

        } catch (BadCredentialsException exception) {

            throw new CustomResponseException(
                    "Email ou mot de passe incorrect",
                    401
            );
        }

        String jwt = jwtUtils.generateJwtToken(email);

        return new JwtResponse(
                jwt,
                email,
                utilisateur.getRole().name()
        );
    }

    public Utilisateur getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return utilisateurRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable")
                );
    }
}
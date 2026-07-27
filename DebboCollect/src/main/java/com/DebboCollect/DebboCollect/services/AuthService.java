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

    public JwtResponse login(
            LoginRequest request
    ) {


        Utilisateur utilisateur =
                utilisateurRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new CustomResponseException(
                                        "Utilisateur introuvable",
                                        404
                                )
                        );

        if (!utilisateur.getCompteActif()) {

            if (utilisateur.getRole() == Role.SUPERVISEUR) {

                throw new CustomResponseException(
                        "Compte désactivé par l'administrateur",
                        500
                );
            }

            if (utilisateur.getRole() == Role.BAILLEUR
                    || utilisateur.getRole() == Role.ENQUETEUR) {

                throw new CustomResponseException(
                        "Compte désactivé par le superviseur",
                        500
                );
            }
        }

        Authentication authentication =
                authenticationManager.authenticate(

                        new UsernamePasswordAuthenticationToken(

                                request.getEmail(),

                                request.getPassword()
                        )
                );

        String jwt =
                jwtUtils.generateJwtToken(
                        request.getEmail()
                );

        return new JwtResponse(
                jwt,
                request.getEmail(),
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
package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.JwtResponse;
import com.DebboCollect.DebboCollect.Model.LoginRequest;
import com.DebboCollect.DebboCollect.Model.RegisterRequest;
import com.DebboCollect.DebboCollect.configs.JwtUtils;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.exception.CustomResponseException;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
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
                request.getEmail()
        );
    }

    public String register(
            RegisterRequest request
    ) {

        if (utilisateurRepository
                .findByEmail(request.getEmail())
                .isPresent()) {

            throw new CustomResponseException(
                    "Email déjà utilisé",
                    400
            );
        }

        Utilisateur utilisateur =
                new Utilisateur();

        utilisateur.setNom(
                request.getNom()
        );

        utilisateur.setEmail(
                request.getEmail()
        );

        utilisateur.setPassword(

                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        utilisateur.setRole(
                request.getRole()
        );

        utilisateurRepository.save(utilisateur);

        return "Utilisateur créé avec succès";
    }
}
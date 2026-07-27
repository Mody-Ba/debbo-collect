package com.DebboCollect.DebboCollect.configs;

import com.DebboCollect.DebboCollect.entity.Role;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_EMAIL:}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Override
    public void run(String... args) {

        if (adminEmail == null || adminEmail.isBlank()
                || adminPassword == null || adminPassword.isBlank()) {

            throw new IllegalStateException(
                    "ADMIN_EMAIL et ADMIN_PASSWORD sont obligatoires"
            );
        }

        Utilisateur admin = utilisateurRepository.findAll()
                .stream()
                .filter(utilisateur ->
                        utilisateur.getRole() == Role.ADMIN
                )
                .findFirst()
                .orElseGet(Utilisateur::new);

        admin.setNom("Administrateur");
        admin.setEmail(adminEmail.trim().toLowerCase());

        if (admin.getPassword() == null
                || !passwordEncoder.matches(
                adminPassword,
                admin.getPassword()
        )) {

            admin.setPassword(
                    passwordEncoder.encode(adminPassword)
            );
        }

        admin.setRole(Role.ADMIN);
        admin.setCompteActif(true);

        utilisateurRepository.save(admin);

        System.out.println(
                "Compte administrateur initial synchronisé"
        );
    }
}
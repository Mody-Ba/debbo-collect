package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.UtilisateurModel;
import com.DebboCollect.DebboCollect.entity.Role;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.mappers.UtilisateurMapper;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurServiceImp implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurServiceImp(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UtilisateurModel createUser(UtilisateurModel model) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String roleConnecte = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (roleConnecte.equals("ROLE_ADMIN")
                && model.getRole() != Role.SUPERVISEUR) {

            throw new RuntimeException("Admin peut créer seulement des superviseurs");
        }

        if (roleConnecte.equals("ROLE_SUPERVISEUR")
                && model.getRole() != Role.ENQUETEUR
                && model.getRole() != Role.BAILLEUR) {

            throw new RuntimeException("Superviseur peut créer seulement enquêteur ou bailleur");
        }
        model.setCompteActif(false);

        model.setPassword(
                passwordEncoder.encode(model.getPassword())
        );

        Utilisateur utilisateur = UtilisateurMapper.toEntity(model);
        if (roleConnecte.equals("ROLE_SUPERVISEUR")) {

            String email = authentication.getName();

            Utilisateur superviseur = utilisateurRepository
                    .findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("Superviseur introuvable"));

            utilisateur.setSuperviseur(superviseur);
        }

        utilisateur = utilisateurRepository.save(utilisateur);

        return UtilisateurMapper.toModel(utilisateur);
    }

    @Override
    public List<UtilisateurModel> getAllUsers() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        String email = authentication.getName();

        Utilisateur connectedUser = utilisateurRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur connecté introuvable"));

        // ================= ADMIN =================

        if (role.equals("ROLE_ADMIN")) {

            return utilisateurRepository.findAll()
                    .stream()
                    .filter(user ->
                            user.getRole() == Role.SUPERVISEUR)
                    .map(UtilisateurMapper::toModel)
                    .toList();
        }

        // ================= SUPERVISEUR =================

        if (role.equals("ROLE_SUPERVISEUR")) {

            return utilisateurRepository.findAll()
                    .stream()
                    .filter(user ->
                            user.getSuperviseur() != null
                                    && user.getSuperviseur().getId()
                                    .equals(connectedUser.getId()))
                    .map(UtilisateurMapper::toModel)
                    .toList();
        }

        return List.of();
    }
    @Override
    public UtilisateurModel getUserById(Long id) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable"));

        if (role.equals("ROLE_ADMIN")
                && utilisateur.getRole() != Role.SUPERVISEUR) {

            throw new RuntimeException(
                    "Admin peut voir seulement les superviseurs"
            );
        }

        if (role.equals("ROLE_SUPERVISEUR")
                && utilisateur.getRole() != Role.BAILLEUR
                && utilisateur.getRole() != Role.ENQUETEUR) {

            throw new RuntimeException(
                    "Superviseur peut voir seulement bailleur ou enquêteur"
            );
        }

        return UtilisateurMapper.toModel(utilisateur);
    }

    @Override
    public UtilisateurModel updateUser(Long id, UtilisateurModel model) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable"));

        if (role.equals("ROLE_ADMIN")
                && utilisateur.getRole() != Role.SUPERVISEUR) {

            throw new RuntimeException(
                    "Admin peut modifier seulement les superviseurs"
            );
        }

        if (role.equals("ROLE_SUPERVISEUR")
                && utilisateur.getRole() != Role.BAILLEUR
                && utilisateur.getRole() != Role.ENQUETEUR) {

            throw new RuntimeException(
                    "Superviseur peut modifier seulement bailleur ou enquêteur"
            );
        }

        utilisateur.setNom(model.getNom());

        utilisateur.setEmail(model.getEmail());

        utilisateur.setPassword(model.getPassword());

        utilisateur.setRole(model.getRole());

        utilisateur = utilisateurRepository.save(utilisateur);

        return UtilisateurMapper.toModel(utilisateur);
    }

    @Override
    public void deleteUser(Long id) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable"));

        if (role.equals("ROLE_ADMIN")
                && utilisateur.getRole() != Role.SUPERVISEUR) {

            throw new RuntimeException(
                    "Admin peut supprimer seulement les superviseurs"
            );
        }

        if (role.equals("ROLE_SUPERVISEUR")
                && utilisateur.getRole() != Role.BAILLEUR
                && utilisateur.getRole() != Role.ENQUETEUR) {

            throw new RuntimeException(
                    "Superviseur peut supprimer seulement bailleur ou enquêteur"
            );
        }

        utilisateurRepository.deleteById(id);
    }
    @Override
    public UtilisateurModel activateUser(Long id) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String roleConnecte =
                authentication.getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority();

        Utilisateur utilisateur =
                utilisateurRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Utilisateur introuvable"));

        // ADMIN peut activer seulement SUPERVISEUR
        if (roleConnecte.equals("ROLE_ADMIN")
                && utilisateur.getRole() != Role.SUPERVISEUR) {

            throw new RuntimeException(
                    "Admin peut activer seulement les superviseurs"
            );
        }

        // SUPERVISEUR peut activer seulement BAILLEUR et ENQUETEUR
        if (roleConnecte.equals("ROLE_SUPERVISEUR")
                && utilisateur.getRole() != Role.BAILLEUR
                && utilisateur.getRole() != Role.ENQUETEUR) {

            throw new RuntimeException(
                    "Superviseur peut activer seulement bailleur ou enquêteur"
            );
        }

        utilisateur.setCompteActif(true);

        utilisateur = utilisateurRepository.save(utilisateur);

        return UtilisateurMapper.toModel(utilisateur);
    }

    @Override
    public UtilisateurModel deactivateUser(Long id) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String roleConnecte =
                authentication.getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority();

        Utilisateur utilisateur =
                utilisateurRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Utilisateur introuvable"));

        // ADMIN peut désactiver seulement SUPERVISEUR
        if (roleConnecte.equals("ROLE_ADMIN")
                && utilisateur.getRole() != Role.SUPERVISEUR) {

            throw new RuntimeException(
                    "Admin peut désactiver seulement les superviseurs"
            );
        }

        // SUPERVISEUR peut désactiver seulement BAILLEUR et ENQUETEUR
        if (roleConnecte.equals("ROLE_SUPERVISEUR")
                && utilisateur.getRole() != Role.BAILLEUR
                && utilisateur.getRole() != Role.ENQUETEUR) {

            throw new RuntimeException(
                    "Superviseur peut désactiver seulement bailleur ou enquêteur"
            );
        }

        utilisateur.setCompteActif(false);

        utilisateur = utilisateurRepository.save(utilisateur);

        return UtilisateurMapper.toModel(utilisateur);
    }

}

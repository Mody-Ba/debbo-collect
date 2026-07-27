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
import com.DebboCollect.DebboCollect.repository.ProjetRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurServiceImp implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProjetRepository projetRepository;

    public UtilisateurServiceImp(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder,ProjetRepository projetRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.projetRepository = projetRepository;
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

        if (roleConnecte.equals("ROLE_SUPERVISEUR")
                && model.getRole() == Role.BAILLEUR) {

            String emailSuperviseur = authentication.getName();

            Utilisateur superviseur = utilisateurRepository
                    .findByEmail(emailSuperviseur)
                    .orElseThrow(() ->
                            new RuntimeException("Superviseur introuvable"));

            String emailBailleur = model.getEmail()
                    .trim()
                    .toLowerCase();

            Utilisateur bailleurExistant = utilisateurRepository
                    .findByEmailIgnoreCase(emailBailleur)
                    .orElse(null);

            if (bailleurExistant != null) {

                if (bailleurExistant.getRole() != Role.BAILLEUR) {
                    throw new RuntimeException(
                            "Cet email appartient déjà à un autre utilisateur"
                    );
                }

                superviseur.getBailleursAssocies()
                        .add(bailleurExistant);

                utilisateurRepository.save(superviseur);

                return UtilisateurMapper.toModel(bailleurExistant);
            }
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

        if (roleConnecte.equals("ROLE_SUPERVISEUR")
                && utilisateur.getRole() == Role.BAILLEUR) {

            Utilisateur superviseur = utilisateur.getSuperviseur();

            superviseur.getBailleursAssocies()
                    .add(utilisateur);

            utilisateurRepository.save(superviseur);
        }

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



        if (role.equals("ROLE_SUPERVISEUR")) {

            return utilisateurRepository.findAll()
                    .stream()
                    .filter(user -> {

                        boolean enqueteurDuSuperviseur =
                                user.getRole() == Role.ENQUETEUR
                                        && user.getSuperviseur() != null
                                        && user.getSuperviseur().getId()
                                        .equals(connectedUser.getId());

                        boolean bailleurAssocie =
                                user.getRole() == Role.BAILLEUR
                                        && connectedUser.getBailleursAssocies()
                                        .stream()
                                        .anyMatch(bailleur ->
                                                bailleur.getId().equals(user.getId()));

                        return enqueteurDuSuperviseur || bailleurAssocie;
                    })
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

        // ADMIN peut modifier seulement les superviseurs
        if (role.equals("ROLE_ADMIN")
                && utilisateur.getRole() != Role.SUPERVISEUR) {

            throw new RuntimeException(
                    "Admin peut modifier seulement les superviseurs"
            );
        }

        // SUPERVISEUR peut modifier seulement bailleurs et enquêteurs
        if (role.equals("ROLE_SUPERVISEUR")
                && utilisateur.getRole() != Role.BAILLEUR
                && utilisateur.getRole() != Role.ENQUETEUR) {

            throw new RuntimeException(
                    "Superviseur peut modifier seulement bailleur ou enquêteur"
            );
        }
        if (role.equals("ROLE_SUPERVISEUR")
                && utilisateur.getRole() == Role.BAILLEUR) {

            String emailSuperviseur = authentication.getName();

            Utilisateur superviseur = utilisateurRepository
                    .findByEmail(emailSuperviseur)
                    .orElseThrow(() ->
                            new RuntimeException("Superviseur introuvable"));

            Long bailleurId = utilisateur.getId();

            boolean bailleurAssocie = superviseur
                    .getBailleursAssocies()
                    .stream()
                    .anyMatch(bailleur ->
                            bailleur.getId().equals(bailleurId));

            if (!bailleurAssocie) {
                throw new RuntimeException(
                        "Ce bailleur ne vous appartient pas"
                );
            }

            long nombreSuperviseurs =
                    utilisateurRepository
                            .countSuperviseursAssociesAuBailleur(
                                    bailleurId
                            );

            if (nombreSuperviseurs > 1) {
                throw new RuntimeException(
                        "Impossible de modifier ce bailleur : il est partagé avec un autre superviseur"
                );
            }
        }

        // Mise à jour des champs autorisés
        utilisateur.setNom(model.getNom());
        utilisateur.setEmail(model.getEmail());
        utilisateur.setCompteActif(model.getCompteActif());
        // On ne touche ni au rôle ni au mot de passe

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

        if (role.equals("ROLE_SUPERVISEUR")
                && utilisateur.getRole() == Role.BAILLEUR) {

            String emailSuperviseur = authentication.getName();

            Utilisateur superviseur = utilisateurRepository
                    .findByEmail(emailSuperviseur)
                    .orElseThrow(() ->
                            new RuntimeException("Superviseur introuvable"));

            boolean bailleurAssocie = superviseur
                    .getBailleursAssocies()
                    .stream()
                    .anyMatch(bailleur ->
                            bailleur.getId().equals(utilisateur.getId()));

            if (!bailleurAssocie) {
                throw new RuntimeException(
                        "Ce bailleur ne vous appartient pas"
                );
            }

            boolean possedeProjet =
                    projetRepository.existsBySuperviseurIdAndBailleurId(
                            superviseur.getId(),
                            utilisateur.getId()
                    );

            if (possedeProjet) {
                throw new RuntimeException(
                        "Impossible de retirer ce bailleur : il est associé à un projet"
                );
            }

            superviseur.getBailleursAssocies()
                    .removeIf(bailleur ->
                            bailleur.getId().equals(utilisateur.getId()));

            utilisateurRepository.save(superviseur);

            return;
        }
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

        if (roleConnecte.equals("ROLE_SUPERVISEUR")
                && utilisateur.getRole() == Role.BAILLEUR) {

            String emailSuperviseur = authentication.getName();

            Utilisateur superviseur = utilisateurRepository
                    .findByEmail(emailSuperviseur)
                    .orElseThrow(() ->
                            new RuntimeException("Superviseur introuvable"));

            Long bailleurId = utilisateur.getId();

            boolean bailleurAssocie = superviseur
                    .getBailleursAssocies()
                    .stream()
                    .anyMatch(bailleur ->
                            bailleur.getId().equals(bailleurId));

            if (!bailleurAssocie) {
                throw new RuntimeException(
                        "Ce bailleur ne vous appartient pas"
                );
            }

            long nombreSuperviseurs =
                    utilisateurRepository
                            .countSuperviseursAssociesAuBailleur(
                                    bailleurId
                            );

            if (nombreSuperviseurs > 1) {
                throw new RuntimeException(
                        "Impossible de désactiver ce bailleur : il est partagé avec un autre superviseur"
                );
            }
        }

        utilisateur = utilisateurRepository.save(utilisateur);

        return UtilisateurMapper.toModel(utilisateur);
    }

}

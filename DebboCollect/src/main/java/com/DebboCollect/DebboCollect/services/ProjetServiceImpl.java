package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.ProjetRequest;
import com.DebboCollect.DebboCollect.Model.ProjetResponse;
import com.DebboCollect.DebboCollect.entity.Projet;
import com.DebboCollect.DebboCollect.entity.Role;
import com.DebboCollect.DebboCollect.entity.StatusCollect;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.mappers.ProjetMapper;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjetServiceImpl implements ProjetService {
    private final UtilisateurRepository utilisateurRepository;

    private final ProjetRepository projetRepository;

    private final ProjetMapper projetMapper;

    @Override
    public ProjetResponse creerProjet(ProjetRequest request) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        Utilisateur superviseur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Superviseur introuvable"));

        Utilisateur bailleur = utilisateurRepository.findById(request.getBailleurId())
                .orElseThrow(() -> new RuntimeException("Bailleur non trouvé"));

        Projet projet = projetMapper.toEntity(request);

        projet.setSuperviseur(superviseur);

        projet.setBailleur(bailleur);

        Projet savedProjet = projetRepository.save(projet);

        return projetMapper.toResponse(savedProjet);
    }

    @Override
    public List<ProjetResponse> afficherTousLesProjets() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        String email = authentication.getName();

        if (role.equals("ROLE_BAILLEUR")) {

            return projetRepository.findAll()
                    .stream()
                    .filter(projet -> projet.getBailleur().getEmail().equals(email))
                    .filter(projet -> projet.getCollectes()
                            .stream()
                            .anyMatch(c -> c.getStatut() == StatusCollect.VALIDEE))
                    .map(projetMapper::toResponse)
                    .toList();
        }

        if (role.equals("ROLE_SUPERVISEUR")) {

            return projetRepository.findAll()
                    .stream()
                    .filter(projet ->
                            projet.getSuperviseur()
                                    .getEmail()
                                    .equals(email))
                    .map(projetMapper::toResponse)
                    .toList();
        }

        throw new RuntimeException("Accès refusé");
    }

    @Override
    public ProjetResponse afficherProjetParId(Long id) {

        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (role.equals("ROLE_BAILLEUR")) {

            boolean collecteValidee = projet.getCollectes()
                    .stream()
                    .anyMatch(c -> c.getStatut() == StatusCollect.VALIDEE);

            if (!collecteValidee) {

                throw new RuntimeException("Projet non accessible");
            }
        }

        return projetMapper.toResponse(projet);
    }

    @Override
    public ProjetResponse modifierProjet(Long id,
                                         ProjetRequest request) {

        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        projetMapper.updateEntityFromRequest(request, projet);

        Projet updatedProjet = projetRepository.save(projet);

        return projetMapper.toResponse(updatedProjet);
    }

    @Override
    public void supprimerProjet(Long id) {

        projetRepository.deleteById(id);
    }

    @Override
    public ProjetResponse assignerEnqueteur(
            Long projetId,
            Long enqueteurId
    ) {

        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() ->
                        new RuntimeException("Projet non trouvé"));

        Utilisateur enqueteur = utilisateurRepository.findById(enqueteurId)
                .orElseThrow(() ->
                        new RuntimeException("Enquêteur non trouvé"));

        if (enqueteur.getRole() != Role.ENQUETEUR) {

            throw new RuntimeException(
                    "Cet utilisateur n'est pas un enquêteur"
            );
        }

        if (!enqueteur.getCompteActif()) {

            throw new RuntimeException(
                    "Le compte de l'enquêteur est désactivé"
            );
        }

        if (enqueteur.getProjet() != null) {

            throw new RuntimeException(
                    "Enquêteur déjà assigné à un projet"
            );
        }

        enqueteur.setProjet(projet);

        utilisateurRepository.save(enqueteur);

        return projetMapper.toResponse(projet);
    }
}

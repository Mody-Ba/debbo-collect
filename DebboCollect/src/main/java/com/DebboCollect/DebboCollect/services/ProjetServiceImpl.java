package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.ProjetDashboardResponse;
import com.DebboCollect.DebboCollect.Model.ProjetRequest;
import com.DebboCollect.DebboCollect.Model.ProjetResponse;
import com.DebboCollect.DebboCollect.entity.*;
import com.DebboCollect.DebboCollect.mappers.ProjetMapper;
import com.DebboCollect.DebboCollect.repository.CollectRepository;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import com.DebboCollect.DebboCollect.repository.ReponseRepository;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjetServiceImpl implements ProjetService {
    private final UtilisateurRepository utilisateurRepository;

    private final ProjetRepository projetRepository;

    private final ProjetMapper projetMapper;

    private final CollectRepository collecteRepository;

    private final ReponseRepository reponseRepository;

    private final AuthService authService;

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
        if (role.equals("ROLE_ENQUETEUR")) {

            return projetRepository.findAll()
                    .stream()
                    .filter(projet ->
                            projet.getEnqueteurs()
                                    .stream()
                                    .anyMatch(e ->
                                            e.getEmail().equals(email)
                                    )
                    )
                    .map(projetMapper::toResponse)
                    .toList();
        }

        throw new RuntimeException("Accès refusé");
    }

    @Override
    public ProjetResponse afficherProjetParId(Long id) {

        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        if (role.equals("ROLE_BAILLEUR")) {

            String email = authentication.getName();

            if (projet.getBailleur() == null ||
                    !projet.getBailleur().getEmail().equals(email)) {

                throw new RuntimeException("Projet non accessible");
            }

            if (projet.getDateEnvoiBailleur() == null) {

                throw new RuntimeException("Projet pas encore envoyé au bailleur");
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

        if (projet.getStatut() == StatutProjet.TERMINE) {
            throw new RuntimeException(
                    "Impossible d'assigner un enquêteur à un projet terminé"
            );
        }

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

    @Override
    public List<ProjetResponse> afficherMesProjets() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        Utilisateur enqueteur =
                utilisateurRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("Enquêteur introuvable"));

        if (enqueteur.getProjet() == null) {

            return List.of();
        }

        return List.of(
                projetMapper.toResponse(
                        enqueteur.getProjet()
                )
        );
    }

    @Override
    public List<Map<String, Object>> exporterProjet(Long projetId) {

        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() ->
                        new RuntimeException("Projet introuvable"));

        List<Collecte> collectes =
                collecteRepository.findByProjetId(projetId);

        List<Map<String, Object>> resultat = new ArrayList<>();

        for (Collecte collecte : collectes) {

            Map<String, Object> ligne = new LinkedHashMap<>();

            ligne.put("Collecte",
                    collecte.getNumeroCollecteProjet());

            ligne.put("Enquêteur",
                    collecte.getEnqueteur().getNom());

            ligne.put("Date",
                    collecte.getDateCollecte());

            ligne.put("Statut",
                    collecte.getStatut());

            List<Reponse> reponses =
                    reponseRepository.findByCollecteId(
                            collecte.getId());

            for (Reponse r : reponses) {

                ligne.put(
                        r.getChamp().getQuestion(),
                        r.getValeur()
                );
            }

            resultat.add(ligne);
        }

        return resultat;
    }

    @Override
    public ProjetResponse envoyerAuBailleur(Long id) {

        Projet projet = projetRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Projet introuvable"));

        List<Collecte> nouvellesCollectes = projet.getCollectes()
                .stream()
                .filter(c -> c.getStatut() == StatusCollect.VALIDEE)
                .filter(c ->

                        projet.getDateEnvoiBailleur() == null

                                ||

                                (
                                        c.getDateValidation() != null
                                                &&
                                                c.getDateValidation().isAfter(
                                                        projet.getDateEnvoiBailleur()
                                                )
                                )

                )
                .toList();

        if (nouvellesCollectes.isEmpty()) {
            throw new RuntimeException(
                    "Aucune nouvelle collecte validée à envoyer."
            );
        }

        projet.setDateEnvoiBailleur(LocalDateTime.now());

        Projet savedProjet = projetRepository.save(projet);

        return projetMapper.toResponse(savedProjet);
    }
    @Override
    public List<ProjetResponse> getProjetsEnvoyesAuBailleur() {

        Utilisateur bailleur = authService.getCurrentUser();

        List<Projet> projets =
                projetRepository.findByBailleurIdAndDateEnvoiBailleurIsNotNull(
                        bailleur.getId()
                );

        return projets.stream()
                .map(projetMapper::toResponse)
                .toList();
    }

    @Override
    public ProjetDashboardResponse getDashboardProjet(Long projetId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() ->
                        new RuntimeException("Projet introuvable"));

        if (projet.getBailleur() == null
                || !projet.getBailleur().getEmail().equals(email)) {

            throw new RuntimeException("Accès refusé");
        }

        if (projet.getDateEnvoiBailleur() == null) {

            throw new RuntimeException(
                    "Projet non envoyé au bailleur"
            );
        }

        long nombreCollectes =
                projet.getCollectes()
                        .stream()
                        .filter(c -> c.getStatut() == StatusCollect.VALIDEE)
                        .count();

        long nombreReponses =
                projet.getCollectes()
                        .stream()
                        .filter(c -> c.getStatut() == StatusCollect.VALIDEE)
                        .mapToLong(c -> c.getReponses().size())
                        .sum();

        return ProjetDashboardResponse.builder()
                .nombreCollectes(nombreCollectes)
                .nombreReponses(nombreReponses)
                .build();
    }

    @Override
    @Transactional
    public ProjetResponse terminerProjet(Long id) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String emailSuperviseur = authentication.getName();

        Projet projet = projetRepository
                .findByIdAndSuperviseurEmail(
                        id,
                        emailSuperviseur
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Projet introuvable ou accès refusé"
                        ));

        if (projet.getStatut() == StatutProjet.TERMINE) {
            throw new RuntimeException(
                    "Ce projet est déjà terminé"
            );
        }

        if (projet.getDateFin() == null
                || LocalDate.now().isBefore(projet.getDateFin())) {

            throw new RuntimeException(
                    "Le projet ne peut pas être terminé avant sa date de fin"
            );
        }

        List<Utilisateur> enqueteurs =
                new ArrayList<>(projet.getEnqueteurs());

        for (Utilisateur enqueteur : enqueteurs) {
            enqueteur.setProjet(null);
        }

        utilisateurRepository.saveAll(enqueteurs);

        projet.getEnqueteurs().clear();
        projet.setStatut(StatutProjet.TERMINE);

        Projet projetTermine =
                projetRepository.save(projet);

        return projetMapper.toResponse(projetTermine);
    }


}

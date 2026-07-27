package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.CollectRequest;
import com.DebboCollect.DebboCollect.Model.CollectResponse;
import com.DebboCollect.DebboCollect.entity.Collecte;
import com.DebboCollect.DebboCollect.entity.Projet;
import com.DebboCollect.DebboCollect.entity.StatusCollect;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.mappers.CollectMapper;
import com.DebboCollect.DebboCollect.repository.CollectRepository;
import com.DebboCollect.DebboCollect.repository.LotCollecteRepository;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectServiceImp implements CollectService {

    private final CollectRepository collectRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ProjetRepository projetRepository;
    private final CollectMapper collectMapper;
    private final LotCollecteRepository lotCollecteRepository;

    @Override
    public CollectResponse creerCollecte(CollectRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Utilisateur enqueteur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur non trouvé"));

        Projet projet = projetRepository.findById(request.getProjetId())
                .orElseThrow(() ->
                        new RuntimeException("Projet non trouvé"));

        Collecte collecte =
                collectMapper.toEntity(request, enqueteur, projet);

        collecte.setStatut(StatusCollect.ENREGISTREE);

        Integer numeroCollecte =
                collectRepository.countByProjetId(projet.getId()) + 1;

        collecte.setNumeroCollecteProjet(numeroCollecte);

        Collecte savedCollecte =
                collectRepository.save(collecte);

        return collectMapper.toResponse(savedCollecte);
    }

    @Override
    public List<CollectResponse> afficherToutesLesCollectes() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        String email = authentication.getName();

        if (role.equals("ROLE_ENQUETEUR")) {

            return collectRepository.findAll()
                    .stream()
                    .filter(c -> c.getEnqueteur().getEmail().equals(email))
                    .map(collectMapper::toResponse)
                    .toList();
        }
        if (role.equals("ROLE_SUPERVISEUR")) {

            return collectRepository.findAll()
                    .stream()
                    .filter(c ->
                            c.getProjet()
                                    .getSuperviseur()
                                    .getEmail()
                                    .equals(email)
                    )
                    .map(collectMapper::toResponse)
                    .toList();
        }

        if (role.equals("ROLE_BAILLEUR")) {

            return collectRepository.findAll()
                    .stream()
                    .filter(c -> c.getProjet().getBailleur().getEmail().equals(email))
                    .filter(c -> c.getStatut() == StatusCollect.VALIDEE)
                    .map(collectMapper::toResponse)
                    .toList();
        }

        return collectRepository.findAll()
                .stream()
                .map(collectMapper::toResponse)
                .toList();
    }

    @Override
    public CollectResponse afficherCollecteParId(Long id) {

        Collecte collecte = collectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collecte non trouvée"));

        return collectMapper.toResponse(collecte);
    }

    @Override
    public CollectResponse modifierCollecte(Long id, CollectRequest request) {

        Collecte collecte = collectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collecte non trouvée"));

        if (collecte.getStatut() == StatusCollect.VALIDEE) {
            throw new RuntimeException("Impossible de modifier une collecte validée");
        }

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        if (!collecte.getEnqueteur().getEmail().equals(email)) {

            throw new RuntimeException(
                    "Vous ne pouvez pas modifier cette collecte"
            );
        }

        Utilisateur enqueteur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Projet projet = projetRepository.findById(request.getProjetId())
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        collecte.setDateCollecte(request.getDateCollecte());
        collecte.setEnqueteur(enqueteur);
        collecte.setProjet(projet);

        Collecte updatedCollecte = collectRepository.save(collecte);

        return collectMapper.toResponse(updatedCollecte);
    }

    @Override
    public void supprimerCollecte(Long id) {

        collectRepository.deleteById(id);
    }

    @Override
    public CollectResponse validerCollecte(Long id) {

        Collecte collecte = collectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collecte introuvable"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        if (!collecte.getProjet()
                .getSuperviseur()
                .getEmail()
                .equals(email)) {

            throw new RuntimeException(
                    "Vous ne pouvez pas valider cette collecte"
            );
        }

        collecte.setStatut(StatusCollect.VALIDEE);

        collecte.setDateValidation(LocalDateTime.now());

        return collectMapper.toResponse(
                collectRepository.save(collecte)
        );
    }
    @Override
    public CollectResponse demanderRevision(Long id) {

        Collecte collecte = collectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collecte introuvable"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        if (!collecte.getProjet()
                .getSuperviseur()
                .getEmail()
                .equals(email)) {

            throw new RuntimeException(
                    "Vous ne pouvez pas demander une révision"
            );
        };


        collecte.setStatut(StatusCollect.EN_REVISION);

        return collectMapper.toResponse(
                collectRepository.save(collecte)
        );
    }

    @Override
    public CollectResponse envoyerCollecte(Long id) {

        Collecte collecte = collectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collecte introuvable"));

        if (collecte.getStatut() == StatusCollect.EN_ATTENTE) {
            throw new RuntimeException("Collecte déjà envoyée");
        }

        collecte.setStatut(StatusCollect.EN_ATTENTE);

        return collectMapper.toResponse(
                collectRepository.save(collecte)
        );
    }



}
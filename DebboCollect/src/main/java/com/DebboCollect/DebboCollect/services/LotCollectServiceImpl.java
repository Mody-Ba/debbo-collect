package com.DebboCollect.DebboCollect.services;
import com.DebboCollect.DebboCollect.Model.LotCollectRequest;
import com.DebboCollect.DebboCollect.Model.LotCollectResponse;
import com.DebboCollect.DebboCollect.entity.*;
import com.DebboCollect.DebboCollect.mappers.LotCollectMapper;
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
public class LotCollectServiceImpl implements LotCollectService{

    private final LotCollecteRepository lotCollecteRepository;
    private final CollectRepository collectRepository;
    private final ProjetRepository projetRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final LotCollectMapper lotCollecteMapper;

    @Override
    public LotCollectResponse creerLot(LotCollectRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Utilisateur enqueteur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Projet projet = projetRepository.findById(request.getProjetId())
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        List<Collecte> collectes = collectRepository.findAll()
                .stream()
                .filter(c -> c.getEnqueteur().getId().equals(enqueteur.getId()))
                .filter(c -> c.getProjet().getId().equals(projet.getId()))
                .filter(c ->
                c.getStatut() == StatusCollect.ENREGISTREE ||
                        c.getStatut() == StatusCollect.EN_REVISION
        )
                .toList();

        if (collectes.isEmpty()) {
            throw new RuntimeException("Aucune collecte enregistrée à envoyer");
        }

        var lot = lotCollecteMapper.toEntity(projet, enqueteur);

        var savedLot = lotCollecteRepository.save(lot);

        collectes.forEach(collecte -> {
            collecte.setLotCollecte(savedLot);
            collecte.setStatut(StatusCollect.EN_ATTENTE);
            collectRepository.save(collecte);
        });

        return lotCollecteMapper.toResponse(
                savedLot,
                collectes.size()
        );
    }

    @Override
    public List<LotCollectResponse> afficherTousLesLots() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        String email = authentication.getName();

        if (role.equals("ROLE_ENQUETEUR")) {

            return lotCollecteRepository.findAll()
                    .stream()
                    .filter(lot ->
                            lot.getEnqueteur()
                                    .getEmail()
                                    .equals(email)
                    )
                    .map(lot -> lotCollecteMapper.toResponse(
                            lot,
                            lot.getCollectes().size()
                    ))
                    .toList();
        }

        if (role.equals("ROLE_SUPERVISEUR")) {

            return lotCollecteRepository.findAll()
                    .stream()
                    .filter(lot ->
                            lot.getProjet()
                                    .getSuperviseur()
                                    .getEmail()
                                    .equals(email)
                    )
                    .map(lot -> lotCollecteMapper.toResponse(
                            lot,
                            lot.getCollectes().size()
                    ))
                    .toList();
        }

        return List.of();
    }

    @Override
    public LotCollectResponse afficherLotParId(Long id) {

        LotCollect lot = lotCollecteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Lot introuvable"));

        return lotCollecteMapper.toResponse(
                lot,
                lot.getCollectes().size()
        );
    }

    @Override
    public LotCollectResponse validerLot(Long id) {

        LotCollect lot = lotCollecteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Lot introuvable"));

        if (lot.getStatut() == StatusCollect.VALIDEE) {
            throw new RuntimeException("Ce lot est déjà validé");
        }

        if (lot.getStatut() == StatusCollect.EN_REVISION) {
            throw new RuntimeException("Ce lot est déjà en révision");
        }

        lot.setStatut(StatusCollect.VALIDEE);

        lot.getCollectes().forEach(collecte -> {
            collecte.setStatut(StatusCollect.VALIDEE);
            collecte.setDateValidation(LocalDateTime.now());
        });

        collectRepository.saveAll(lot.getCollectes());

        LotCollect savedLot = lotCollecteRepository.save(lot);

        return lotCollecteMapper.toResponse(
                savedLot,
                savedLot.getCollectes().size()
        );
    }

    @Override
    public LotCollectResponse demanderRevisionLot(Long id) {

        LotCollect lot = lotCollecteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Lot introuvable"));

        if (lot.getStatut() == StatusCollect.EN_REVISION) {
            throw new RuntimeException("Ce lot est déjà en révision");
        }

        if (lot.getStatut() == StatusCollect.VALIDEE) {
            throw new RuntimeException("Ce lot est déjà validé");
        }

        lot.getCollectes().forEach(collecte -> {

            boolean aCommentaire = collecte.getReponses()
                    .stream()
                    .anyMatch(reponse ->
                            reponse.getCommentaireSuperviseur() != null &&
                                    !reponse.getCommentaireSuperviseur().isBlank()
                    );

            if (aCommentaire) {
                collecte.setStatut(StatusCollect.EN_REVISION);
            }

        });

        collectRepository.saveAll(lot.getCollectes());

        LotCollect savedLot = lotCollecteRepository.save(lot);

        return lotCollecteMapper.toResponse(
                savedLot,
                savedLot.getCollectes().size()
        );
    }


}

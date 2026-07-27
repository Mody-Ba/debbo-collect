package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.ChampRequest;
import com.DebboCollect.DebboCollect.Model.ChampResponse;
import com.DebboCollect.DebboCollect.entity.Champ;
import com.DebboCollect.DebboCollect.entity.Projet;
import com.DebboCollect.DebboCollect.mappers.ChampMapper;
import com.DebboCollect.DebboCollect.repository.ChampRepository;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.DebboCollect.DebboCollect.entity.TypeChamps;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChampServiceImp implements ChampService {

    private final ChampRepository champRepository;
    private final ProjetRepository projetRepository;
    private final ChampMapper champMapper;


    @Override
    public ChampResponse creerChamp(ChampRequest request) {

        System.out.println(
                "STATISTIQUE RECUE = " + request.getStatistique()
        );
        System.out.println("REQUEST = " + request);

        Projet projet = projetRepository
                .findById(request.getProjetId())
                .orElseThrow(() ->
                        new RuntimeException("Projet non trouvé")
                );

        if (
                (request.getType() == TypeChamps.CHOIX_UNIQUE
                        || request.getType() == TypeChamps.CHOIX_MULTIPLE)
                        && (
                        request.getOptions() == null
                                || request.getOptions().isBlank()
                )
        ) {
            throw new RuntimeException(
                    "Les options sont obligatoires pour ce type de champ"
            );
        }

        Champ champ = champMapper.toEntity(request, projet);

        if (request.getChampParentId() != null) {

            Champ champParent = champRepository
                    .findById(request.getChampParentId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Question principale introuvable"
                            )
                    );

            if (!champParent.getProjet().getId()
                    .equals(projet.getId())) {

                throw new RuntimeException(
                        "La question principale doit appartenir au même projet"
                );
            }

            if (champParent.getType() != TypeChamps.OUI_NON) {

                throw new RuntimeException(
                        "La question principale doit être de type Oui/Non"
                );
            }

            String valeur = request.getValeurDeclenchement();

            if (
                    !"true".equalsIgnoreCase(valeur)
                            && !"false".equalsIgnoreCase(valeur)
            ) {
                throw new RuntimeException(
                        "La réponse déclencheuse doit être Oui ou Non"
                );
            }

            champ.setChampParent(champParent);
            champ.setValeurDeclenchement(
                    valeur.toLowerCase()
            );

        } else {

            champ.setChampParent(null);
            champ.setValeurDeclenchement(null);
        }

        Champ savedChamp = champRepository.save(champ);

        return champMapper.toResponse(savedChamp);
    }
    @Override
    public List<ChampResponse> afficherTousLesChamps() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        String email = authentication.getName();

        if (role.equals("ROLE_ENQUETEUR")) {

            return champRepository.findAll()
                    .stream()
                    .filter(champ -> champ.getProjet()
                            .getCollectes()
                            .stream()
                            .anyMatch(collecte ->
                                    collecte.getEnqueteur()
                                            .getEmail()
                                            .equals(email)
                            )
                    )
                    .map(champMapper::toResponse)
                    .toList();
        }

        return champRepository.findAll()
                .stream()
                .map(champMapper::toResponse)
                .toList();
    }
    @Override
    public ChampResponse afficherChampParId(Long id) {

        Champ champ = champRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Champ non trouvé"));

        return champMapper.toResponse(champ);
    }

    @Override
    public ChampResponse modifierChamp(Long id, ChampRequest request) {

        Champ champ = champRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Champ non trouvé"));

        Projet projet = projetRepository.findById(request.getProjetId())
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        champ.setType(request.getType());
        champ.setQuestion(request.getQuestion());
        champ.setOptions(request.getOptions());
        champ.setPreuveObligatoire(request.isPreuveObligatoire());
        champ.setStatistique(request.getStatistique());
        champ.setProjet(projet);

        Champ updatedChamp = champRepository.save(champ);

        return champMapper.toResponse(updatedChamp);
    }

    @Override
    public void supprimerChamp(Long id) {

        champRepository.deleteById(id);
    }

    @Override
    public List<ChampResponse> afficherChampsParProjet(Long projetId) {

        return champRepository.findByProjetId(projetId)
                .stream()
                .map(champMapper::toResponse)
                .toList();
    }
}
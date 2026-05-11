package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.ChampRequest;
import com.DebboCollect.DebboCollect.Model.ChampResponse;
import com.DebboCollect.DebboCollect.entity.Champ;
import com.DebboCollect.DebboCollect.entity.Projet;
import com.DebboCollect.DebboCollect.mappers.ChampMapper;
import com.DebboCollect.DebboCollect.repository.ChampRepository;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChampServiceImp implements ChampService {

    private final ChampRepository champRepository;
    private final ProjetRepository projetRepository;
    private final ChampMapper champMapper;

    @Override
    public ChampResponse creerChamp(ChampRequest request) {

        Projet projet = projetRepository.findById(request.getProjetId())
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        Champ champ = champMapper.toEntity(request, projet);

        Champ savedChamp = champRepository.save(champ);

        return champMapper.toResponse(savedChamp);
    }

    @Override
    public List<ChampResponse> afficherTousLesChamps() {

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
        champ.setPreuveObligatoire(request.isPreuveObligatoire());
        champ.setProjet(projet);

        Champ updatedChamp = champRepository.save(champ);

        return champMapper.toResponse(updatedChamp);
    }

    @Override
    public void supprimerChamp(Long id) {

        champRepository.deleteById(id);
    }
}
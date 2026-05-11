package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.ProjetRequest;
import com.DebboCollect.DebboCollect.Model.ProjetResponse;
import com.DebboCollect.DebboCollect.entity.Projet;
import com.DebboCollect.DebboCollect.mappers.ProjetMapper;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjetServiceImpl implements ProjetService {

    private final ProjetRepository projetRepository;
    private final ProjetMapper projetMapper;

    @Override
    public ProjetResponse creerProjet(ProjetRequest request) {

        Projet projet = projetMapper.toEntity(request);

        Projet savedProjet = projetRepository.save(projet);

        return projetMapper.toResponse(savedProjet);
    }

    @Override
    public List<ProjetResponse> afficherTousLesProjets() {

        return projetRepository.findAll()
                .stream()
                .map(projetMapper::toResponse)
                .toList();
    }

    @Override
    public ProjetResponse afficherProjetParId(Long id) {

        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        return projetMapper.toResponse(projet);
    }

    @Override
    public ProjetResponse modifierProjet(Long id, ProjetRequest request) {

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
}

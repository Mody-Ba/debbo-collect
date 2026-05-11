package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.CollectRequest;
import com.DebboCollect.DebboCollect.Model.CollectResponse;
import com.DebboCollect.DebboCollect.entity.Collecte;
import com.DebboCollect.DebboCollect.entity.Projet;
import com.DebboCollect.DebboCollect.entity.Utilisateur;
import com.DebboCollect.DebboCollect.mappers.CollectMapper;
import com.DebboCollect.DebboCollect.repository.CollectRepository;
import com.DebboCollect.DebboCollect.repository.ProjetRepository;
import com.DebboCollect.DebboCollect.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectServiceImp implements CollectService {

    private final CollectRepository collectRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ProjetRepository projetRepository;
    private final CollectMapper collectMapper;

    @Override
    public CollectResponse creerCollecte(CollectRequest request) {

        Utilisateur enqueteur = utilisateurRepository.findById(request.getEnqueteurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Projet projet = projetRepository.findById(request.getProjetId())
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        Collecte collecte = collectMapper.toEntity(request, enqueteur, projet);

        Collecte savedCollecte = collectRepository.save(collecte);

        return collectMapper.toResponse(savedCollecte);
    }

    @Override
    public List<CollectResponse> afficherToutesLesCollectes() {

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

        Utilisateur enqueteur = utilisateurRepository.findById(request.getEnqueteurId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Projet projet = projetRepository.findById(request.getProjetId())
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        collecte.setDateCollecte(request.getDateCollecte());
        collecte.setLocalisation(request.getLocalisation());
        collecte.setEnqueteur(enqueteur);
        collecte.setProjet(projet);

        Collecte updatedCollecte = collectRepository.save(collecte);

        return collectMapper.toResponse(updatedCollecte);
    }

    @Override
    public void supprimerCollecte(Long id) {

        collectRepository.deleteById(id);
    }
}
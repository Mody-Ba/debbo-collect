package com.DebboCollect.DebboCollect.services;

import com.DebboCollect.DebboCollect.Model.ReponseRequest;
import com.DebboCollect.DebboCollect.Model.ReponseResponse;
import com.DebboCollect.DebboCollect.entity.Champ;
import com.DebboCollect.DebboCollect.entity.Collecte;
import com.DebboCollect.DebboCollect.entity.Reponse;
import com.DebboCollect.DebboCollect.mappers.ReponseMapper;
import com.DebboCollect.DebboCollect.repository.ChampRepository;
import com.DebboCollect.DebboCollect.repository.CollectRepository;
import com.DebboCollect.DebboCollect.repository.ReponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReponseServiceImp implements ReponseService {

    private final ReponseRepository reponseRepository;
    private final ChampRepository champRepository;
    private final CollectRepository collecteRepository;
    private final ReponseMapper reponseMapper;

    @Override
    public ReponseResponse creerReponse(ReponseRequest request) {

        Champ champ = champRepository.findById(request.getChampId())
                .orElseThrow(() -> new RuntimeException("Champ non trouvé"));

        Collecte collecte = collecteRepository.findById(request.getCollecteId())
                .orElseThrow(() -> new RuntimeException("Collecte non trouvée"));

        Reponse reponse = reponseMapper.toEntity(request, champ, collecte);

        Reponse savedReponse = reponseRepository.save(reponse);

        return reponseMapper.toResponse(savedReponse);
    }

    @Override
    public List<ReponseResponse> afficherToutesLesReponses() {

        return reponseRepository.findAll()
                .stream()
                .map(reponseMapper::toResponse)
                .toList();
    }

    @Override
    public ReponseResponse afficherReponseParId(Long id) {

        Reponse reponse = reponseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réponse non trouvée"));

        return reponseMapper.toResponse(reponse);
    }

    @Override
    public ReponseResponse modifierReponse(Long id, ReponseRequest request) {

        Reponse reponse = reponseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réponse non trouvée"));

        Champ champ = champRepository.findById(request.getChampId())
                .orElseThrow(() -> new RuntimeException("Champ non trouvé"));

        Collecte collecte = collecteRepository.findById(request.getCollecteId())
                .orElseThrow(() -> new RuntimeException("Collecte non trouvée"));

        reponse.setValeur(request.getValeur());
        reponse.setChamp(champ);
        reponse.setCollecte(collecte);

        Reponse updatedReponse = reponseRepository.save(reponse);

        return reponseMapper.toResponse(updatedReponse);
    }

    @Override
    public void supprimerReponse(Long id) {

        reponseRepository.deleteById(id);
    }
}
package com.DebboCollect.DebboCollect.mappers;

import com.DebboCollect.DebboCollect.Model.ReponseModel;
import com.DebboCollect.DebboCollect.Model.ReponseRequest;
import com.DebboCollect.DebboCollect.Model.ReponseResponse;
import com.DebboCollect.DebboCollect.entity.Champ;
import com.DebboCollect.DebboCollect.entity.Collecte;
import com.DebboCollect.DebboCollect.entity.Reponse;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
public class ReponseMapper {

    public Reponse toEntity(ReponseRequest request,
                            Champ champ,
                            Collecte collecte) {

        return Reponse.builder()
                .valeur(request.getValeur())
                .champ(champ)
                .collecte(collecte)
                .build();
    }

    public ReponseResponse toResponse(Reponse reponse) {

        return ReponseResponse.builder()
                .id(reponse.getId())
                .valeur(reponse.getValeur())
                .champId(reponse.getChamp().getId())
                .collecteId(reponse.getCollecte().getId())
                .build();
    }

    public ReponseModel toModel(Reponse reponse) {

        return ReponseModel.builder()
                .id(reponse.getId())
                .valeur(reponse.getValeur())
                .champId(reponse.getChamp().getId())
                .collecteId(reponse.getCollecte().getId())
                .build();
    }
}
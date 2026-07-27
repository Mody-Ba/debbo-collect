package com.DebboCollect.DebboCollect.mappers;

import com.DebboCollect.DebboCollect.Model.ChampModel;
import com.DebboCollect.DebboCollect.Model.ChampRequest;
import com.DebboCollect.DebboCollect.Model.ChampResponse;
import com.DebboCollect.DebboCollect.entity.Champ;
import com.DebboCollect.DebboCollect.entity.Projet;
import org.springframework.stereotype.Component;

import static org.springframework.http.RequestEntity.options;

@Component
public class ChampMapper {

    public Champ toEntity(ChampRequest request, Projet projet) {

        return Champ.builder()
                .type(request.getType())
                .question(request.getQuestion())
                .options(request.getOptions())
                .preuveObligatoire(request.isPreuveObligatoire())
                .statistique(request.getStatistique())
                .valeurDeclenchement(request.getValeurDeclenchement())
                .projet(projet)
                .build();
    }

    public ChampResponse toResponse(Champ champ) {

        return ChampResponse.builder()
                .id(champ.getId())
                .type(champ.getType())
                .question(champ.getQuestion())
                .options(champ.getOptions())
                .preuveObligatoire(champ.isPreuveObligatoire())
                .statistique(champ.getStatistique())
                .champParentId(
                        champ.getChampParent() != null
                                ? champ.getChampParent().getId()
                                : null
                )
                .valeurDeclenchement(champ.getValeurDeclenchement())
                .projetId(champ.getProjet().getId())
                .build();
    }


    public ChampModel toModel(Champ champ) {

        return ChampModel.builder()
                .id(champ.getId())
                .type(champ.getType())
                .question(champ.getQuestion())
                .options(champ.getOptions())
                .preuveObligatoire(champ.isPreuveObligatoire())
                .statistique(champ.getStatistique())
                .projetId(champ.getProjet().getId())
                .build();
    }

}

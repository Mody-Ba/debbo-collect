package com.DebboCollect.DebboCollect.Model;

import com.DebboCollect.DebboCollect.entity.TypeChamps;
import com.DebboCollect.DebboCollect.entity.TypeStatistique;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ChampResponse {

    private Long id;

    private TypeChamps type;

    private String options;

    private String question;

    private boolean preuveObligatoire;

    private TypeStatistique statistique;

    private Long champParentId;

    private String valeurDeclenchement;

    private Long projetId;
}

package com.DebboCollect.DebboCollect.Model;

import com.DebboCollect.DebboCollect.entity.TypeChamps;
import com.DebboCollect.DebboCollect.entity.TypeStatistique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

public class ChampRequest {
    @NotNull(message = "Le type est obligatoire")
    private TypeChamps type;
    private String options;
    @NotBlank(message = "La question est obligatoire")

    private String question;
    @NotNull
    private boolean preuveObligatoire;

    private TypeStatistique statistique;

    private Long champParentId;

    private String valeurDeclenchement;

    @NotNull
    private Long projetId;
}

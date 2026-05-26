package com.DebboCollect.DebboCollect.Model;

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
    @NotBlank(message = "Le type est obligatoire")
    private String type;
    @NotBlank(message = "La question est obligatoire")
    private String question;
    @NotNull
    private boolean preuveObligatoire;
    @NotNull
    private Long projetId;
}

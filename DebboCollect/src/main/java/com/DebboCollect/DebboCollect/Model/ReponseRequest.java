package com.DebboCollect.DebboCollect.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReponseRequest {
    @NotBlank(message = "La valeur de la réponse est obligatoire")
    private String valeur;
    @NotNull(message = "Le champ est obligatoire")
    private Long champId;
    @NotNull(message = "La collecte est obligatoire")
    private Long collecteId;
}
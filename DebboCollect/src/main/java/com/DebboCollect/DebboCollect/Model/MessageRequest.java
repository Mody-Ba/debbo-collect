package com.DebboCollect.DebboCollect.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest {
    @NotBlank(message = "Le contenu du message est obligatoire")
    private String contenu;
    @NotNull(message = "Le statut lu est obligatoire")
    private boolean lu;
    @NotNull(message = "L'expéditeur est obligatoire")
    private Long expediteurId;
    @NotNull(message = "Le destinataire est obligatoire")
    private Long destinataireId;
}
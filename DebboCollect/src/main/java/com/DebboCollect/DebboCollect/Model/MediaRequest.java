package com.DebboCollect.DebboCollect.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaRequest {
    @NotBlank(message = "Le type du média est obligatoire")
    private String type;
    @NotBlank(message = "L'URL du média est obligatoire")
    private String url;
    @NotNull(message = "La réponse est obligatoire")
    private Long reponseId;
}
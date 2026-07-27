package com.DebboCollect.DebboCollect.Model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationResponse {

    private Long utilisateurId;

    private String nom;

    private String role;

    private String dernierMessage;

}

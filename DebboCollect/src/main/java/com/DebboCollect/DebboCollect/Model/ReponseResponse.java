package com.DebboCollect.DebboCollect.Model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReponseResponse {

    private Long id;

    private String valeur;

    private Long champId;

    private Long collecteId;
}
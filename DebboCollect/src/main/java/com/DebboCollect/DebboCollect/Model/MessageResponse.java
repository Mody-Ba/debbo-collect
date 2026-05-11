package com.DebboCollect.DebboCollect.Model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {

    private Long id;

    private String contenu;

    private Date dateEnvoi;

    private boolean lu;

    private Long expediteurId;

    private Long destinataireId;
}
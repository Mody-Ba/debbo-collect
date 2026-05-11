package com.DebboCollect.DebboCollect.Model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaResponse {

    private Long id;

    private String type;

    private String url;

    private Long reponseId;
}
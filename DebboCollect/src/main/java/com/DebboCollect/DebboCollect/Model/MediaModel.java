package com.DebboCollect.DebboCollect.Model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaModel {

    private Long id;

    private String type;

    private String url;

    private Long reponseId;
}
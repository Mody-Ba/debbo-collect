package com.DebboCollect.DebboCollect.Model;

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

public class ChampModel {

    private Long id;

    private String type;

    private String question;

    private boolean preuveObligatoire;

    private Long projetId;
}

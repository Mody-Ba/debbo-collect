package com.DebboCollect.DebboCollect.Model;

import java.util.List;

import com.DebboCollect.DebboCollect.entity.TypeChamps;
import com.DebboCollect.DebboCollect.entity.TypeStatistique;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionStatModel {
    private Long champId;

    private String question;

    private TypeChamps type;

    private TypeStatistique statistique;

    private List<ReponseStatModel> reponses;
}

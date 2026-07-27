package com.DebboCollect.DebboCollect.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DashboardDynamicModel {
    private List<DashboardCardModel> cartes;

    private List<LocalisationStatModel> regions;

    private List<QuestionStatModel> questions;

}

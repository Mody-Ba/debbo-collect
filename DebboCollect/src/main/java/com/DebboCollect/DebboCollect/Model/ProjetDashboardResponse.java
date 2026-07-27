package com.DebboCollect.DebboCollect.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjetDashboardResponse {

    private long nombreCollectes;

    private long nombreReponses;

}
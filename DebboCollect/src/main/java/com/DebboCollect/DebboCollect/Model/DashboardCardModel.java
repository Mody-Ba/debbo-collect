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

public class DashboardCardModel {
    private String titre;

    private Long nombre;

    private Double pourcentage;
}

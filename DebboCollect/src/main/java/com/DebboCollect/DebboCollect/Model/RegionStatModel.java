package com.DebboCollect.DebboCollect.Model;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegionStatModel {
    private String region;

    private Integer nombre;

    private Double pourcentage;
}

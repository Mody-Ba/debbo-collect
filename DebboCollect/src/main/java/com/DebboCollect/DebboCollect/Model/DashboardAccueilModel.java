package com.DebboCollect.DebboCollect.Model;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardAccueilModel {
    private Integer nombreProjets;
    private Integer nombreEnqueteurs;
    private Integer nombreBailleurs;
    private Integer nombreCollectes;
}

package mg.tomamiarilaza.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueByRouteId implements Serializable {

    private Integer year;
    private Integer month;
    private Integer idOrigine;
    private Integer idDestination;
}
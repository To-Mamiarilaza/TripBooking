package mg.tomamiarilaza.restapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Immutable
@Subselect("SELECT * FROM v_revenue_by_route")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RevenueByRouteId.class)
public class RevenueByRoute {

    @Id
    @Column(name = "year")
    private Integer year;

    @Id
    @Column(name = "month")
    private Integer month;

    @Id
    @Column(name = "id_origine")
    private Integer idOrigine;

    @Id
    @Column(name = "id_destination")
    private Integer idDestination;

    @Column(name = "origine")
    private String origine;

    @Column(name = "destination")
    private String destination;

    @Column(name = "nb_voyage")
    private Long nbVoyage;

    @Column(name = "nb_chaise_reserve")
    private Long nbChaiseReserve;

    @Column(name = "total_revenue")
    private Double totalRevenue;
}

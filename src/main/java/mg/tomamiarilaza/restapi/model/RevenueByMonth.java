package mg.tomamiarilaza.restapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Immutable
@Subselect("SELECT * FROM v_revenue_by_month")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueByMonth {
    @Id
    @Column(name = "year_month")
    private String yearMonth;

    @Column(name = "year")
    private Integer year;

    @Column(name = "month")
    private Integer month;

    @Column(name = "total_reservations")
    private Long totalReservations;

    @Column(name = "total_seats_booked")
    private Long totalSeatsBooked;

    @Column(name = "total_revenue")
    private Double totalRevenue;
}

package mg.tomamiarilaza.restapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.time.LocalDateTime;

@Entity
@Immutable
@Subselect("SELECT * FROM v_voyage_seat_states")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoyageSeatState {
    @Id
    @Column(name = "seat_id")
    private Integer seatId;

    @Column(name = "voyage_id")
    private Integer voyageId;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "origin")
    private String origin;

    @Column(name = "destination")
    private String destination;

    @Column(name = "price")
    private Double price;

    @Column(name = "car_number")
    private String carNumber;

    @Column(name = "car_brand")
    private String carBrand;

    @Column(name = "seat_number")
    private Integer seatNumber;

    @Column(name = "seat_status")
    private String seatStatus;

    @Column(name = "passenger_name")
    private String passengerName;

    @Column(name = "passenger_phone")
    private String passengerPhone;

    @Column(name = "reservation_state")
    private String reservationState;

    @Column(name = "voyage_state")
    private Integer voyageState;

    @Column(name = "seat_state")
    private Integer seatState;
}

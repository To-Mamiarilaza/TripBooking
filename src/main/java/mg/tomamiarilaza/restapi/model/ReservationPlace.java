package mg.tomamiarilaza.restapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation_place")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_reservation", nullable = false)
    @JsonBackReference
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "id_place", nullable = false)
    private PlaceVoiture place;

    @Column(nullable = false)
    private Integer etat;
}

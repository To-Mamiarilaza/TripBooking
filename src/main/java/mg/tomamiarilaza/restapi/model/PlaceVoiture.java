package mg.tomamiarilaza.restapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "place_voiture")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceVoiture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_voiture", nullable = false)
    private Voiture voiture;

    @Column(nullable = false)
    private Integer numero;

    @Column(nullable = false)
    private Integer etat;
}

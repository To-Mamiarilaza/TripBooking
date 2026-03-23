package mg.tomamiarilaza.restapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tarif")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarif {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_origine", nullable = false)
    private Lieu origine;

    @ManyToOne
    @JoinColumn(name = "id_destination", nullable = false)
    private Lieu destination;

    @Column(nullable = false)
    private Double prix;
}

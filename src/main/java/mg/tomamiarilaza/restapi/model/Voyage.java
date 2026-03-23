package mg.tomamiarilaza.restapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "voyage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voyage {
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

    @Column(nullable = false)
    private LocalDateTime depart;

    @ManyToOne
    @JoinColumn(name = "id_voiture", nullable = false)
    private Voiture voiture;

    @ManyToOne
    @JoinColumn(name = "id_chauffeur", nullable = false)
    private Chauffeur chauffeur;

    @Column(nullable = false)
    private Integer etat;
}

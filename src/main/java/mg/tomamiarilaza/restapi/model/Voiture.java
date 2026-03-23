package mg.tomamiarilaza.restapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "voiture")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voiture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String marque;

    @Column(nullable = false)
    private Integer etat;
}

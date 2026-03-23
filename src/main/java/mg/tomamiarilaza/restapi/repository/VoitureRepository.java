package mg.tomamiarilaza.restapi.repository;

import mg.tomamiarilaza.restapi.model.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoitureRepository extends JpaRepository<Voiture, Integer> {
}

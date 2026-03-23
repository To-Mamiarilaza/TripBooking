package mg.tomamiarilaza.restapi.repository;

import mg.tomamiarilaza.restapi.model.Chauffeur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChauffeurRepository extends JpaRepository<Chauffeur, Integer> {
}

package mg.tomamiarilaza.restapi.repository;

import mg.tomamiarilaza.restapi.model.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VoyageRepository extends JpaRepository<Voyage, Integer>, JpaSpecificationExecutor<Voyage> {
}

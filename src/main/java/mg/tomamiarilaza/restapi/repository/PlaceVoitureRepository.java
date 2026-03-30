package mg.tomamiarilaza.restapi.repository;

import mg.tomamiarilaza.restapi.model.PlaceVoiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceVoitureRepository extends JpaRepository<PlaceVoiture, Integer> {
    List<PlaceVoiture> findByVoitureId(Integer voitureId);
    Optional<PlaceVoiture> findByVoitureIdAndNumero(Integer voitureId, Integer numero);
}

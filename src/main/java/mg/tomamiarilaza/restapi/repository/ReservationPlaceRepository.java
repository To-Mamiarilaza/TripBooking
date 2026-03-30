package mg.tomamiarilaza.restapi.repository;

import mg.tomamiarilaza.restapi.model.ReservationPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationPlaceRepository extends JpaRepository<ReservationPlace, Integer> {
    List<ReservationPlace> findByReservationId(Integer reservationId);
    ReservationPlace findByReservationIdAndPlaceId(Integer reservationId, Integer placeId);
}

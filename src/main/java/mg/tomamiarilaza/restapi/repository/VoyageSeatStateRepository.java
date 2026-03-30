package mg.tomamiarilaza.restapi.repository;

import mg.tomamiarilaza.restapi.model.VoyageSeatState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoyageSeatStateRepository extends JpaRepository<VoyageSeatState, Integer> {
    /**
     * Get all seat states for a specific voyage
     */
    List<VoyageSeatState> findByVoyageId(Integer voyageId);

    /**
     * Get specific seat state
     */
    Optional<VoyageSeatState> findBySeatIdAndVoyageId(Integer seatId, Integer voyageId);

    /**
     * Check available seats for a voyage (seats with AVAILABLE status)
     */
    @Query(value = "SELECT * FROM v_voyage_seat_states WHERE voyage_id = :voyageId AND seat_status = 'AVAILABLE'", nativeQuery = true)
    List<VoyageSeatState> findAvailableSeatsByVoyageId(@Param("voyageId") Integer voyageId);

    /**
     * Check reserved seats for a voyage
     */
    @Query(value = "SELECT * FROM v_voyage_seat_states WHERE voyage_id = :voyageId AND seat_status = 'RESERVED'", nativeQuery = true)
    List<VoyageSeatState> findReservedSeatsByVoyageId(@Param("voyageId") Integer voyageId);
}

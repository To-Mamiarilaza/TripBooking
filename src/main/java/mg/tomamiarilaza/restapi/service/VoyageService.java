package mg.tomamiarilaza.restapi.service;

import jakarta.persistence.criteria.Predicate;
import mg.tomamiarilaza.restapi.dto.VoyageDTO;
import mg.tomamiarilaza.restapi.dto.VoyageWithDetailsDTO;
import mg.tomamiarilaza.restapi.model.Voyage;
import mg.tomamiarilaza.restapi.model.VoyageSeatState;
import mg.tomamiarilaza.restapi.model.Reservation;
import mg.tomamiarilaza.restapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoyageService {

    @Autowired VoyageRepository voyageRepository;
    @Autowired LieuRepository lieuRepository;
    @Autowired VoitureRepository voitureRepository;
    @Autowired ChauffeurRepository chauffeurRepository;
    @Autowired VoyageSeatStateRepository voyageSeatStateRepository;
    @Autowired ReservationRepository reservationRepository;

    public Voyage create(VoyageDTO dto) {
        Voyage voyage = new Voyage();
        voyage.setOrigine(lieuRepository.findById(dto.getIdOrigine())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lieu origine introuvable")));
        voyage.setDestination(lieuRepository.findById(dto.getIdDestination())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lieu destination introuvable")));
        voyage.setPrix(dto.getPrix());
        voyage.setDepart(dto.getDepart());
        voyage.setVoiture(voitureRepository.findById(dto.getIdVoiture())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voiture introuvable")));
        voyage.setChauffeur(chauffeurRepository.findById(dto.getIdChauffeur())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chauffeur introuvable")));
        voyage.setEtat(1);
        return voyageRepository.save(voyage);
    }

    public Voyage update(Integer id, VoyageDTO dto) {
        Voyage voyage = voyageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage introuvable"));

        voyage.setOrigine(lieuRepository.findById(dto.getIdOrigine())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lieu origine introuvable")));
        voyage.setDestination(lieuRepository.findById(dto.getIdDestination())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lieu destination introuvable")));
        voyage.setPrix(dto.getPrix());
        voyage.setDepart(dto.getDepart());
        voyage.setVoiture(voitureRepository.findById(dto.getIdVoiture())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voiture introuvable")));
        voyage.setChauffeur(chauffeurRepository.findById(dto.getIdChauffeur())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chauffeur introuvable")));
        return voyageRepository.save(voyage);
    }

    public void cancel(Integer id) {
        Voyage voyage = voyageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage introuvable"));
        voyage.setEtat(0);
        voyageRepository.save(voyage);
    }

    public List<Voyage> findAll(Integer idOrigine, Integer idDestination, LocalDate depart, Integer etat) {
        Specification<Voyage> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (idOrigine != null) {
                predicates.add(cb.equal(root.get("origine").get("id"), idOrigine));
            }
            if (idDestination != null) {
                predicates.add(cb.equal(root.get("destination").get("id"), idDestination));
            }
            if (depart != null) {
                predicates.add(cb.equal(
                        root.get("depart").as(java.sql.Date.class),
                        java.sql.Date.valueOf(depart)
                ));
            }
            if (etat != null) {
                predicates.add(cb.equal(root.get("etat"), etat));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return voyageRepository.findAll(spec);
    }

    /**
     * Get voyage with all details: seats from view and reservations
     * Avoids recursive fetching by using DTOs
     */
    public VoyageWithDetailsDTO getVoyageWithDetails(Integer voyageId) {
        Voyage voyage = voyageRepository.findById(voyageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voyage introuvable"));

        // Get all seats for this voyage from the view
        List<VoyageSeatState> seatStates = voyageSeatStateRepository.findByVoyageId(voyageId);

        // Get all reservations for this voyage
        List<Reservation> reservations = reservationRepository.findByVoyageId(voyageId);

        // Build the response DTO
        VoyageWithDetailsDTO dto = new VoyageWithDetailsDTO();
        dto.setId(voyage.getId());
        dto.setIdOrigine(voyage.getOrigine().getId());
        dto.setIdDestination(voyage.getDestination().getId());
        dto.setOriginName(voyage.getOrigine().getNom());
        dto.setDestinationName(voyage.getDestination().getNom());
        dto.setPrix(voyage.getPrix());
        dto.setDepart(voyage.getDepart());
        dto.setIdVoiture(voyage.getVoiture().getId());
        dto.setVoitureNumero(voyage.getVoiture().getNumero());
        dto.setVoitureMarque(voyage.getVoiture().getMarque());
        dto.setIdChauffeur(voyage.getChauffeur().getId());
        dto.setChauffeurNom(voyage.getChauffeur().getNom());
        dto.setEtat(voyage.getEtat());

        // Map seat states
        List<VoyageWithDetailsDTO.VoyageSeatStateDTO> seatDTOs = seatStates.stream()
                .map(seat -> {
                    VoyageWithDetailsDTO.VoyageSeatStateDTO seatDTO = new VoyageWithDetailsDTO.VoyageSeatStateDTO();
                    seatDTO.setSeatId(seat.getSeatId());
                    seatDTO.setSeatNumber(seat.getSeatNumber());
                    seatDTO.setSeatStatus(seat.getSeatStatus());
                    seatDTO.setSeatState(seat.getSeatState());
                    seatDTO.setPassengerName(seat.getPassengerName());
                    seatDTO.setPassengerPhone(seat.getPassengerPhone());
                    seatDTO.setReservationState(seat.getReservationState());
                    return seatDTO;
                })
                .collect(Collectors.toList());
        dto.setSeats(seatDTOs);

        // Map reservations without loading the full entity relationships
        List<VoyageWithDetailsDTO.ReservationSummaryDTO> reservationDTOs = reservations.stream()
                .map(res -> {
                    VoyageWithDetailsDTO.ReservationSummaryDTO resDTO = new VoyageWithDetailsDTO.ReservationSummaryDTO();
                    resDTO.setId(res.getId());
                    resDTO.setNomVoyageur(res.getNomVoyageur());
                    resDTO.setTelephone(res.getTelephone());
                    resDTO.setEtat(res.getEtat());
                    // Get seat numbers for this reservation
                    if (res.getReservationPlaces() != null) {
                        resDTO.setSeatNumbers(res.getReservationPlaces().stream()
                                .map(rp -> rp.getPlace().getNumero())
                                .collect(Collectors.toList()));
                    }
                    return resDTO;
                })
                .collect(Collectors.toList());
        dto.setReservations(reservationDTOs);

        return dto;
    }
}

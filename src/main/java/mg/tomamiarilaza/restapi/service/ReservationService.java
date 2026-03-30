package mg.tomamiarilaza.restapi.service;

import mg.tomamiarilaza.restapi.dto.ReservationDTO;
import mg.tomamiarilaza.restapi.dto.ReservationPlaceDTO;
import mg.tomamiarilaza.restapi.model.PlaceVoiture;
import mg.tomamiarilaza.restapi.model.Reservation;
import mg.tomamiarilaza.restapi.model.ReservationPlace;
import mg.tomamiarilaza.restapi.model.Voyage;
import mg.tomamiarilaza.restapi.model.VoyageSeatState;
import mg.tomamiarilaza.restapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationPlaceRepository reservationPlaceRepository;

    @Autowired
    private VoyageRepository voyageRepository;

    @Autowired
    private PlaceVoitureRepository placeVoitureRepository;

    @Autowired
    private VoyageSeatStateRepository voyageSeatStateRepository;

    /**
     * Create a new reservation with seats
     * Uses v_voyage_seat_states view to validate seat availability
     */
    @Transactional
    public Reservation createReservation(ReservationDTO dto) {
        // Verify voyage exists
        Voyage voyage = voyageRepository.findById(dto.getIdVoyage())
                .orElseThrow(() -> new RuntimeException("Voyage not found with id: " + dto.getIdVoyage()));

        // Get all seats for this voyage from the view
        List<VoyageSeatState> voyageSeats = voyageSeatStateRepository.findByVoyageId(voyage.getId());
        if (voyageSeats.isEmpty()) {
            throw new RuntimeException("No seats found for voyage: " + voyage.getId());
        }

        // Verify all requested seats exist and are available
        for (ReservationPlaceDTO placeDto : dto.getPlaces()) {
            VoyageSeatState seatState = voyageSeats.stream()
                    .filter(s -> s.getSeatNumber().equals(placeDto.getSeatNumber()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Seat number " + placeDto.getSeatNumber() + " not found in voyage"));

            // Check if seat is available based on view status
            if (!"AVAILABLE".equals(seatState.getSeatStatus())) {
                throw new RuntimeException("Seat " + seatState.getSeatNumber() + " is not available. Current status: " + seatState.getSeatStatus());
            }

            // Check if seat state allows booking (state = 1 means available)
            if (seatState.getSeatState() != 1) {
                throw new RuntimeException("Seat " + seatState.getSeatNumber() + " state does not allow booking (state: " + seatState.getSeatState() + ")");
            }
        }

        // Create reservation
        Reservation reservation = new Reservation();
        reservation.setVoyage(voyage);
        reservation.setNomVoyageur(dto.getNomVoyageur());
        reservation.setTelephone(dto.getTelephone());
        reservation.setEtat(dto.getEtat() != null ? dto.getEtat() : 1);

        Reservation savedReservation = reservationRepository.save(reservation);

        // Create reservation places
        for (ReservationPlaceDTO placeDto : dto.getPlaces()) {
            PlaceVoiture place = placeVoitureRepository
                    .findByVoitureIdAndNumero(voyage.getVoiture().getId(), placeDto.getSeatNumber())
                    .orElseThrow(() -> new RuntimeException("Seat number " + placeDto.getSeatNumber() + " not found in vehicle"));

            ReservationPlace reservationPlace = new ReservationPlace();
            reservationPlace.setReservation(savedReservation);
            reservationPlace.setPlace(place);
            reservationPlace.setEtat(placeDto.getEtat() != null ? placeDto.getEtat() : 1);

            reservationPlaceRepository.save(reservationPlace);
        }

        return savedReservation;
    }

    /**
     * Get all reservations for a specific voyage
     */
    public List<Reservation> getReservationsByVoyage(Integer voyageId) {
        return reservationRepository.findByVoyageId(voyageId);
    }

    /**
     * Get reservation with all its seats
     */
    public Reservation getReservationWithSeats(Integer reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + reservationId));
    }

    /**
     * Get all available seats for a voyage from the view
     */
    public List<VoyageSeatState> getAvailableSeats(Integer voyageId) {
        return voyageSeatStateRepository.findAvailableSeatsByVoyageId(voyageId);
    }

    /**
     * Get all reserved seats for a voyage from the view
     */
    public List<VoyageSeatState> getReservedSeats(Integer voyageId) {
        return voyageSeatStateRepository.findReservedSeatsByVoyageId(voyageId);
    }

    /**
     * Get all seat states for a voyage
     */
    public List<VoyageSeatState> getAllVoyageSeats(Integer voyageId) {
        return voyageSeatStateRepository.findByVoyageId(voyageId);
    }

    /**
     * Update a reservation with new seats
     * Can only update if reservation is PENDING (etat = 1)
     * Allows modifying seats including keeping the same seats
     */
    @Transactional
    public Reservation updateReservation(Integer reservationId, ReservationDTO dto) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + reservationId));

        // Check if reservation can be modified (only PENDING reservations)
        if (reservation.getEtat() != 1) {
            throw new RuntimeException("Cannot modify reservation. Current state: " + getStateName(reservation.getEtat()));
        }

        // Verify voyage exists
        Voyage voyage = voyageRepository.findById(dto.getIdVoyage())
                .orElseThrow(() -> new RuntimeException("Voyage not found with id: " + dto.getIdVoyage()));

        // Get all seats for this voyage from the view
        List<VoyageSeatState> voyageSeats = voyageSeatStateRepository.findByVoyageId(voyage.getId());
        if (voyageSeats.isEmpty()) {
            throw new RuntimeException("No seats found for voyage: " + voyage.getId());
        }

        // Get current reservation places to check if seats belong to this reservation
        List<ReservationPlace> currentPlaces = reservationPlaceRepository.findByReservationId(reservationId);
        List<Integer> currentSeatIds = currentPlaces.stream()
                .map(rp -> rp.getPlace().getId())
                .toList();

        // Verify all requested seats exist and are available (or already belong to this reservation)
        for (ReservationPlaceDTO placeDto : dto.getPlaces()) {
            VoyageSeatState seatState = voyageSeats.stream()
                    .filter(s -> s.getSeatNumber().equals(placeDto.getSeatNumber()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Seat number " + placeDto.getSeatNumber() + " not found in voyage"));

            // Seat is available if it's AVAILABLE or if it belongs to this reservation
            boolean isAvailable = "AVAILABLE".equals(seatState.getSeatStatus());
            boolean belongsToThisReservation = currentSeatIds.contains(seatState.getSeatId());

            if (!isAvailable && !belongsToThisReservation) {
                throw new RuntimeException("Seat " + seatState.getSeatNumber() + " is not available. Current status: " + seatState.getSeatStatus());
            }
        }

        // Update reservation basic info
        reservation.setVoyage(voyage);
        reservation.setNomVoyageur(dto.getNomVoyageur());
        reservation.setTelephone(dto.getTelephone());

        // Delete old reservation places BEFORE saving
        if (reservation.getReservationPlaces() != null) {
            reservation.getReservationPlaces().clear();
        }
        reservationPlaceRepository.deleteAll(currentPlaces);

        Reservation updatedReservation = reservationRepository.save(reservation);

        // Create new reservation places
        for (ReservationPlaceDTO placeDto : dto.getPlaces()) {
            PlaceVoiture place = placeVoitureRepository
                    .findByVoitureIdAndNumero(voyage.getVoiture().getId(), placeDto.getSeatNumber())
                    .orElseThrow(() -> new RuntimeException("Seat number " + placeDto.getSeatNumber() + " not found in vehicle"));

            ReservationPlace reservationPlace = new ReservationPlace();
            reservationPlace.setReservation(updatedReservation);
            reservationPlace.setPlace(place);
            reservationPlace.setEtat(placeDto.getEtat() != null ? placeDto.getEtat() : 1);

            reservationPlaceRepository.save(reservationPlace);
        }

        return updatedReservation;
    }

    /**
     * Confirm a reservation
     * Can only confirm if reservation is PENDING (etat = 1)
     * Sets etat to 2 (CONFIRMED)
     */
    @Transactional
    public Reservation confirmReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + reservationId));

        if (reservation.getEtat() != 1) {
            throw new RuntimeException("Cannot confirm reservation. Current state: " + getStateName(reservation.getEtat()));
        }

        reservation.setEtat(2); // CONFIRMED
        return reservationRepository.save(reservation);
    }

    /**
     * Cancel a reservation
     * Can only cancel if reservation is PENDING (etat = 1)
     * Sets etat to 0 (CANCELLED)
     */
    @Transactional
    public Reservation cancelReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + reservationId));

        if (reservation.getEtat() != 1) {
            throw new RuntimeException("Cannot cancel reservation. Current state: " + getStateName(reservation.getEtat()));
        }

        reservation.setEtat(0); // CANCELLED
        return reservationRepository.save(reservation);
    }

    /**
     * Get state name from etat value
     */
    private String getStateName(Integer etat) {
        return switch (etat) {
            case 0 -> "CANCELLED";
            case 1 -> "PENDING";
            case 2 -> "CONFIRMED";
            default -> "UNKNOWN";
        };
    }
}
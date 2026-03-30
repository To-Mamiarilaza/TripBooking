package mg.tomamiarilaza.restapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import mg.tomamiarilaza.restapi.dto.ReservationDTO;
import mg.tomamiarilaza.restapi.model.Reservation;
import mg.tomamiarilaza.restapi.service.ReservationService;
import mg.tomamiarilaza.restapi.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TokenService tokenService;

    /**
     * POST /api/reservations
     * Create a new reservation with seats
     *
     * Request body example:
     * {
     *   "idVoyage": 1,
     *   "nomVoyageur": "John Doe",
     *   "telephone": "+261321234567",
     *   "etat": 1,
     *   "places": [
     *     {"seatNumber": 1, "etat": 1},
     *     {"seatNumber": 2, "etat": 1}
     *   ]
     * }
     */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<EntityModel<Reservation>> createReservation(
            @RequestBody ReservationDTO dto,
            HttpServletRequest request) {

        try {
            Reservation reservation = reservationService.createReservation(dto);

            EntityModel<Reservation> model = EntityModel.of(reservation,
                    linkTo(methodOn(ReservationController.class).createReservation(dto, request)).withSelfRel(),
                    linkTo(methodOn(ReservationController.class).getReservation(reservation.getId(), request)).withRel("consulter"),
                    linkTo(methodOn(ReservationController.class).updateReservation(reservation.getId(), dto, request)).withRel("modifier"),
                    linkTo(methodOn(ReservationController.class).confirmReservation(reservation.getId(), request)).withRel("confirmer"),
                    linkTo(methodOn(ReservationController.class).cancelReservation(reservation.getId(), request)).withRel("annuler")
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(model);

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * GET /api/reservations/{id}
     * Get reservation with all its seats
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<EntityModel<Reservation>> getReservation(@PathVariable Integer id, HttpServletRequest request) {
        try {
            Reservation reservation = reservationService.getReservationWithSeats(id);

            EntityModel<Reservation> model = EntityModel.of(reservation,
                    linkTo(methodOn(ReservationController.class).getReservation(id, request)).withSelfRel(),
                    linkTo(methodOn(ReservationController.class).updateReservation(id, null, request)).withRel("modifier"),
                    linkTo(methodOn(ReservationController.class).confirmReservation(id, request)).withRel("confirmer"),
                    linkTo(methodOn(ReservationController.class).cancelReservation(id, request)).withRel("annuler")
            );

            return ResponseEntity.ok(model);

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * PUT /api/reservations/{id}
     * Update a reservation with new seats
     * Can only update if reservation is PENDING
     * 
     * Request body example:
     * {
     *   "idVoyage": 1,
     *   "nomVoyageur": "Jane Doe",
     *   "telephone": "+261321234568",
     *   "places": [
     *     {"seatNumber": 3, "etat": 1},
     *     {"seatNumber": 4, "etat": 1}
     *   ]
     * }
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<EntityModel<Reservation>> updateReservation(
            @PathVariable Integer id,
            @RequestBody ReservationDTO dto,
            HttpServletRequest request) {
        try {
            Reservation reservation = reservationService.updateReservation(id, dto);

            EntityModel<Reservation> model = EntityModel.of(reservation,
                    linkTo(methodOn(ReservationController.class).updateReservation(id, dto, request)).withSelfRel(),
                    linkTo(methodOn(ReservationController.class).getReservation(id, request)).withRel("consulter"),
                    linkTo(methodOn(ReservationController.class).confirmReservation(id, request)).withRel("confirmer"),
                    linkTo(methodOn(ReservationController.class).cancelReservation(id, request)).withRel("annuler")
            );

            return ResponseEntity.ok(model);

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * POST /api/reservations/{id}/confirm
     * Confirm a reservation
     * Can only confirm if reservation is PENDING (etat = 1)
     * Sets reservation state to CONFIRMED (etat = 2)
     */
    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<EntityModel<Reservation>> confirmReservation(
            @PathVariable Integer id,
            HttpServletRequest request) {
        try {
            Reservation reservation = reservationService.confirmReservation(id);

            EntityModel<Reservation> model = EntityModel.of(reservation,
                    linkTo(methodOn(ReservationController.class).confirmReservation(id, request)).withSelfRel(),
                    linkTo(methodOn(ReservationController.class).getReservation(id, request)).withRel("consulter"),
                    linkTo(methodOn(ReservationController.class).updateReservation(id, null, request)).withRel("modifier"),
                    linkTo(methodOn(ReservationController.class).cancelReservation(id, request)).withRel("annuler")
            );

            return ResponseEntity.ok(model);

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * POST /api/reservations/{id}/cancel
     * Cancel a reservation
     * Can only cancel if reservation is PENDING (etat = 1)
     * Sets reservation state to CANCELLED (etat = 0)
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<EntityModel<Reservation>> cancelReservation(
            @PathVariable Integer id,
            HttpServletRequest request) {
        try {
            Reservation reservation = reservationService.cancelReservation(id);

            EntityModel<Reservation> model = EntityModel.of(reservation,
                    linkTo(methodOn(ReservationController.class).cancelReservation(id, request)).withSelfRel(),
                    linkTo(methodOn(ReservationController.class).getReservation(id, request)).withRel("consulter"),
                    linkTo(methodOn(ReservationController.class).updateReservation(id, null, request)).withRel("modifier"),
                    linkTo(methodOn(ReservationController.class).confirmReservation(id, request)).withRel("confirmer")
            );

            return ResponseEntity.ok(model);

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}


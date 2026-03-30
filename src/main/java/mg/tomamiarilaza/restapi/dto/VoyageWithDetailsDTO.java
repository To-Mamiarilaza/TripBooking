package mg.tomamiarilaza.restapi.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VoyageWithDetailsDTO {
    private Integer id;
    private Integer idOrigine;
    private Integer idDestination;
    private String originName;
    private String destinationName;
    private Double prix;
    private LocalDateTime depart;
    private Integer idVoiture;
    private String voitureNumero;
    private String voitureMarque;
    private Integer idChauffeur;
    private String chauffeurNom;
    private Integer etat;
    private List<VoyageSeatStateDTO> seats;
    private List<ReservationSummaryDTO> reservations;

    @Data
    public static class VoyageSeatStateDTO {
        private Integer seatId;
        private Integer seatNumber;
        private String seatStatus;
        private Integer seatState;
        private String passengerName;
        private String passengerPhone;
        private String reservationState;
    }

    @Data
    public static class ReservationSummaryDTO {
        private Integer id;
        private String nomVoyageur;
        private String telephone;
        private Integer etat;
        private List<Integer> seatNumbers;
    }
}

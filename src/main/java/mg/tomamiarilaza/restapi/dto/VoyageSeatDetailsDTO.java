package mg.tomamiarilaza.restapi.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VoyageSeatDetailsDTO {
    private Integer voyageId;
    private LocalDateTime departureTime;
    private String origin;
    private String destination;
    private Double price;
    private String carNumber;
    private String carBrand;
    private Integer seatNumber;
    private Integer seatId;
    private String seatStatus;
    private String passengerName;
    private String passengerPhone;
    private String reservationState;
    private Integer voyageState;
    private Integer seatState;
}

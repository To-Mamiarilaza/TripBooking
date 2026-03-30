package mg.tomamiarilaza.restapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReservationDTO {
    private Integer idVoyage;
    private String nomVoyageur;
    private String telephone;
    private Integer etat;
    private List<ReservationPlaceDTO> places;
}

package mg.tomamiarilaza.restapi.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VoyageDTO {
    private Integer idOrigine;
    private Integer idDestination;
    private Double prix;
    private LocalDateTime depart;
    private Integer idVoiture;
    private Integer idChauffeur;
}

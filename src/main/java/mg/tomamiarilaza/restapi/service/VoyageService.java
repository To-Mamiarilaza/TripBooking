package mg.tomamiarilaza.restapi.service;

import jakarta.persistence.criteria.Predicate;
import mg.tomamiarilaza.restapi.dto.VoyageDTO;
import mg.tomamiarilaza.restapi.model.Voyage;
import mg.tomamiarilaza.restapi.repository.ChauffeurRepository;
import mg.tomamiarilaza.restapi.repository.LieuRepository;
import mg.tomamiarilaza.restapi.repository.VoitureRepository;
import mg.tomamiarilaza.restapi.repository.VoyageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class VoyageService {

    @Autowired VoyageRepository voyageRepository;
    @Autowired LieuRepository lieuRepository;
    @Autowired VoitureRepository voitureRepository;
    @Autowired ChauffeurRepository chauffeurRepository;

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
}

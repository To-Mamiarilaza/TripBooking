package mg.tomamiarilaza.restapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import mg.tomamiarilaza.restapi.dto.VoyageDTO;
import mg.tomamiarilaza.restapi.dto.VoyageWithDetailsDTO;
import mg.tomamiarilaza.restapi.model.Voyage;
import mg.tomamiarilaza.restapi.service.TokenService;
import mg.tomamiarilaza.restapi.service.VoyageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/voyages")
public class VoyageController {

    @Autowired
    VoyageService voyageService;

    @Autowired
    TokenService tokenService;

    // --- POST /api/voyages ---
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<Voyage>> create(@RequestBody VoyageDTO dto, HttpServletRequest request) {
        Voyage voyage = voyageService.create(dto);

        EntityModel<Voyage> model = EntityModel.of(voyage,
                linkTo(methodOn(VoyageController.class).getAll(null, null, null, null, null)).withRel("voyages"),
                linkTo(methodOn(VoyageController.class).getVoyageDetails(voyage.getId())).withRel("details"),
                linkTo(methodOn(VoyageController.class).create(dto, null)).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    // --- PUT /api/voyages/{idVoyage} ---
    @PutMapping("/{idVoyage}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<Voyage>> update(@PathVariable Integer idVoyage,
                                                       @RequestBody VoyageDTO dto,
                                                       HttpServletRequest request) {
        Voyage voyage = voyageService.update(idVoyage, dto);

        EntityModel<Voyage> model = EntityModel.of(voyage,
                linkTo(methodOn(VoyageController.class).update(idVoyage, dto, null)).withSelfRel(),
                linkTo(methodOn(VoyageController.class).getAll(null, null, null, null, null)).withRel("voyages"),
                linkTo(methodOn(VoyageController.class).getVoyageDetails(idVoyage)).withRel("details"),
                linkTo(methodOn(VoyageController.class).cancel(idVoyage, null)).withRel("annuler")
        );

        return ResponseEntity.ok(model);
    }

    // --- DELETE /api/voyages/{idVoyage} ---
    @DeleteMapping("/{idVoyage}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> cancel(@PathVariable Integer idVoyage, HttpServletRequest request) {
        voyageService.cancel(idVoyage);

        return ResponseEntity.ok(java.util.Map.of(
                "message", "Voyage annulé avec succès",
                "_links", java.util.Map.of("voyages", java.util.Map.of("href", linkTo(methodOn(VoyageController.class).getAll(null, null, null, null, null)).toString()))
        ));
    }

    // --- GET /api/voyages ---
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Voyage>>> getAll(
            @RequestParam(required = false) Integer idOrigine,
            @RequestParam(required = false) Integer idDestination,
            @RequestParam(required = false) LocalDate depart,
            @RequestParam(required = false) Integer etat,
            HttpServletRequest request) {

        List<Voyage> voyages = voyageService.findAll(idOrigine, idDestination, depart, etat);

        List<EntityModel<Voyage>> models = voyages.stream()
                .map(v -> EntityModel.of(v,
                        linkTo(methodOn(VoyageController.class).getAll(null, null, null, null, null)).withSelfRel(),
                        linkTo(methodOn(VoyageController.class).getVoyageDetails(v.getId())).withRel("details"),
                        linkTo(methodOn(VoyageController.class).cancel(v.getId(), null)).withRel("annuler")
                ))
                .toList();

        Link selfLink = linkTo(methodOn(VoyageController.class).getAll(idOrigine, idDestination, depart, etat, null)).withSelfRel();
        CollectionModel<EntityModel<Voyage>> collection = CollectionModel.of(models, selfLink);

        return ResponseEntity.ok(collection);
    }

    /**
     * GET /api/voyages/{idVoyage}/details
     * Get voyage with all seat states from view and reservations
     * Avoids recursive fetching using DTOs
     */
    @GetMapping("/{idVoyage}/details")
    public ResponseEntity<?> getVoyageDetails(@PathVariable Integer idVoyage) {
        VoyageWithDetailsDTO voyage = voyageService.getVoyageWithDetails(idVoyage);

        EntityModel<?> model = EntityModel.of(voyage,
                linkTo(methodOn(VoyageController.class).getVoyageDetails(idVoyage)).withSelfRel(),
                linkTo(methodOn(VoyageController.class).getAll(null, null, null, null, null)).withRel("voyages"),
                linkTo(methodOn(VoyageController.class).update(idVoyage, null, null)).withRel("editer"),
                linkTo(methodOn(VoyageController.class).cancel(idVoyage, null)).withRel("annuler")
        );

        return ResponseEntity.ok(model);
    }
}

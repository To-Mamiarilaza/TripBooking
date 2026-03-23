package mg.tomamiarilaza.restapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import mg.tomamiarilaza.restapi.dto.VoyageDTO;
import mg.tomamiarilaza.restapi.model.Voyage;
import mg.tomamiarilaza.restapi.service.TokenService;
import mg.tomamiarilaza.restapi.service.VoyageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // --- Validation token/role ---
    private void requireRole(HttpServletRequest request, String role) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token requis");
        }
        String token = authHeader.substring(7);
        if (!tokenService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalide");
        }
        if (!tokenService.hasRole(token, role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé : rôle " + role + " requis");
        }
    }

    // --- POST /api/voyages ---
    @PostMapping
    public ResponseEntity<EntityModel<Voyage>> create(@RequestBody VoyageDTO dto, HttpServletRequest request) {
        requireRole(request, "ADMIN");

        Voyage voyage = voyageService.create(dto);

        EntityModel<Voyage> model = EntityModel.of(voyage,
                linkTo(methodOn(VoyageController.class).getAll(null, null, null, null, null)).withRel("voyages"),
                linkTo(methodOn(VoyageController.class).create(dto, null)).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    // --- PUT /api/voyages/{idVoyage} ---
    @PutMapping("/{idVoyage}")
    public ResponseEntity<EntityModel<Voyage>> update(@PathVariable Integer idVoyage,
                                                       @RequestBody VoyageDTO dto,
                                                       HttpServletRequest request) {
        requireRole(request, "ADMIN");

        Voyage voyage = voyageService.update(idVoyage, dto);

        EntityModel<Voyage> model = EntityModel.of(voyage,
                linkTo(methodOn(VoyageController.class).update(idVoyage, dto, null)).withSelfRel(),
                linkTo(methodOn(VoyageController.class).getAll(null, null, null, null, null)).withRel("voyages"),
                linkTo(methodOn(VoyageController.class).cancel(idVoyage, null)).withRel("annuler")
        );

        return ResponseEntity.ok(model);
    }

    // --- DELETE /api/voyages/{idVoyage} ---
    @DeleteMapping("/{idVoyage}")
    public ResponseEntity<?> cancel(@PathVariable Integer idVoyage, HttpServletRequest request) {
        requireRole(request, "ADMIN");

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
                        linkTo(methodOn(VoyageController.class).cancel(v.getId(), null)).withRel("annuler")
                ))
                .toList();

        Link selfLink = linkTo(methodOn(VoyageController.class).getAll(idOrigine, idDestination, depart, etat, null)).withSelfRel();
        CollectionModel<EntityModel<Voyage>> collection = CollectionModel.of(models, selfLink);

        return ResponseEntity.ok(collection);
    }
}

package mg.tomamiarilaza.restapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import mg.tomamiarilaza.restapi.service.StatisticService;
import mg.tomamiarilaza.restapi.service.TokenService;
import mg.tomamiarilaza.restapi.model.RevenueByMonth;
import mg.tomamiarilaza.restapi.model.RevenueByRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/statistics")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private TokenService tokenService;

    // --- GET /api/statistics/revenus/{year} ---
    @GetMapping("/revenus/{year}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CollectionModel<RevenueByMonth>> getRevenueByMonth(
            @PathVariable Integer year,
            HttpServletRequest request) {
        
        List<RevenueByMonth> revenues = statisticService.getRevenueByMonth(year);
        
        CollectionModel<RevenueByMonth> model = CollectionModel.of(revenues,
                linkTo(methodOn(VoyageController.class).getAll(null, null, null, null, null)).withRel("voyages")
        );
        
        return ResponseEntity.ok(model);
    }

    // --- GET /api/statistics/trajets/{year}/{month} ---
    @GetMapping("/trajets/{year}/{month}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CollectionModel<RevenueByRoute>> getRevenueByRoute(
            @PathVariable Integer year,
            @PathVariable Integer month,
            HttpServletRequest request) {
        
        // Validate month
        if (month < 1 || month > 12) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le mois doit être entre 1 et 12");
        }
        
        List<RevenueByRoute> routes = statisticService.getRevenueByRoute(year, month);
        
        CollectionModel<RevenueByRoute> model = CollectionModel.of(routes,
                linkTo(methodOn(VoyageController.class).getAll(null, null, null, null, null)).withRel("voyages")
        );
        
        return ResponseEntity.ok(model);
    }
}

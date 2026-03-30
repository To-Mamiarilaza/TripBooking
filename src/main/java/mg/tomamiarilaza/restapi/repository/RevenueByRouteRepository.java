package mg.tomamiarilaza.restapi.repository;

import mg.tomamiarilaza.restapi.model.RevenueByRoute;
import mg.tomamiarilaza.restapi.model.RevenueByRouteId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RevenueByRouteRepository extends JpaRepository<RevenueByRoute, RevenueByRouteId> {
    List<RevenueByRoute> findByYearAndMonth(Integer year, Integer month);
}

package mg.tomamiarilaza.restapi.repository;

import mg.tomamiarilaza.restapi.model.RevenueByMonth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RevenueByMonthRepository extends JpaRepository<RevenueByMonth, String> {
    List<RevenueByMonth> findByYear(Integer year);
}

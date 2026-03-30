package mg.tomamiarilaza.restapi.service;

import mg.tomamiarilaza.restapi.model.RevenueByMonth;
import mg.tomamiarilaza.restapi.repository.RevenueByMonthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatisticService {

    @Autowired
    private RevenueByMonthRepository revenueByMonthRepository;

    public List<RevenueByMonth> getRevenueByMonth(Integer year) {
        List<RevenueByMonth> revenues = revenueByMonthRepository.findByYear(year);
        
        // Create a map for quick lookup
        Map<Integer, RevenueByMonth> revenueMap = new HashMap<>();
        for (RevenueByMonth revenue : revenues) {
            revenueMap.put(revenue.getMonth(), revenue);
        }
        
        // Create result list with all 12 months
        List<RevenueByMonth> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            if (revenueMap.containsKey(month)) {
                result.add(revenueMap.get(month));
            } else {
                // Create a default RevenueByMonth for months with no data
                RevenueByMonth defaultRevenue = new RevenueByMonth();
                defaultRevenue.setYear(year);
                defaultRevenue.setMonth(month);
                defaultRevenue.setYearMonth(String.format("%04d-%02d", year, month));
                defaultRevenue.setTotalReservations(0L);
                defaultRevenue.setTotalSeatsBooked(0L);
                defaultRevenue.setTotalRevenue(0.0);
                result.add(defaultRevenue);
            }
        }
        
        return result;
    }
}

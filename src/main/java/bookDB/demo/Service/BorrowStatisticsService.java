package bookDB.demo.Service;

import bookDB.demo.Repository.BorrowStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BorrowStatisticsService {

    private final BorrowStatisticsRepository borrowStatisticsRepository;

    @Autowired
    public BorrowStatisticsService(BorrowStatisticsRepository borrowStatisticsRepository) {
        this.borrowStatisticsRepository = borrowStatisticsRepository;
    }

    public Map<String, Object> getBorrowStatistics(String startDate, String endDate) {
        return borrowStatisticsRepository.getStatistics(startDate, endDate);
    }
}

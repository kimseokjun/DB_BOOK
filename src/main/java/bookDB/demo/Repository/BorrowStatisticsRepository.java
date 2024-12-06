package bookDB.demo.Repository;

import java.util.Map;

public interface BorrowStatisticsRepository {
    public Map<String, Object> getStatistics(String startDate, String endDate);
}

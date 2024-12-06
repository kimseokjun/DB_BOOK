package bookDB.demo.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BBorrowStatisticsRepository implements BorrowStatisticsRepository{
    private final JdbcTemplate jdbcTemplate;

    public BBorrowStatisticsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
@Override
public Map<String, Object> getStatistics(String startDate, String endDate) {
    Map<String, Object> result = new HashMap<>();

    jdbcTemplate.execute("CALL GET_BORROW_STATISTICS(?, ?, ?, ?)", (CallableStatement cs) -> {
        cs.setDate(1, java.sql.Date.valueOf(startDate)); // String을 java.sql.Date로 변환
        cs.setDate(2, java.sql.Date.valueOf(endDate));
        cs.registerOutParameter(3, java.sql.Types.INTEGER); // 총 대출 도서 수
        cs.registerOutParameter(4, java.sql.Types.VARCHAR); // 가장 많이 대출된 장르

        cs.execute();

        result.put("totalBorrows", cs.getInt(3));
        result.put("mostBorrowedGenre", cs.getString(4));
        return result;
    });

    return result;
}


}

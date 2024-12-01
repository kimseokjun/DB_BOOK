package bookDB.demo.Repository;

import bookDB.demo.Domain.Seat;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;

@Repository
public class seat1Repository implements SeatRepository{
    private final JdbcTemplate jdbcTemplate;

    public seat1Repository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Seat> findAllSeats() {
        String sql = "SELECT seat_id, seat_status FROM Seat";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Seat seat = new Seat();
            seat.setSeatId(rs.getLong("seat_id"));
            seat.setStatus(rs.getString("seat_status"));
            return seat;
        });
    }

    @Override
    public Seat checkSeatStatus(Long seatId) {
        String sql = "{call check_seat_status(?, ?, ?)}";
        return jdbcTemplate.execute(sql, (CallableStatementCallback<? extends Seat>) cs -> {
            cs.setLong(1, seatId);                // IN 파라미터
            cs.registerOutParameter(2, Types.VARCHAR); // OUT 파라미터: 좌석 상태
            cs.registerOutParameter(3, Types.VARCHAR); // OUT 파라미터: 마스킹된 이름
            cs.execute();

            String status = cs.getString(2);
            String maskedName = cs.getString(3);

            // 결과값 확인
            if (status == null) {
                throw new IllegalStateException("Seat status not found for seatId: " + seatId);
            }

            Seat seat = new Seat();
            seat.setSeatId(seatId);
            seat.setStatus(status);
            seat.setMaskedName(maskedName);
            return seat;
        });
    }
}

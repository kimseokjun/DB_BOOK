package bookDB.demo.Repository;

import bookDB.demo.Domain.Seat;
import bookDB.demo.Domain.SeatReservation;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    //죄석 현황 확인
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


    //죄석 예약
    @Override
    public int reserveSeat(Integer memberId, Integer seatId) {
        // SQL 호출을 위한 PL/SQL 프로시저 호출
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("SEAT_RESERVATION")
                .declareParameters(
                        new SqlParameter("R_MEMBER_ID", Types.INTEGER),  // 1번 파라미터
                        new SqlParameter("R_SEAT_ID", Types.INTEGER),    // 2번 파라미터
                        new SqlOutParameter("RESERVATION_RESULT", Types.INTEGER) // 3번 파라미터 (결과 값)
                );

// 파라미터 설정
        Map<String, Object> params = new HashMap<>();
        params.put("R_MEMBER_ID", memberId);
        params.put("R_SEAT_ID", seatId);

// 결과 반환
        Map<String, Object> result = jdbcCall.execute(params);
        // 프로시저 실행 후 반환된 값
        return (int)result.get("RESERVATION_RESULT");
    }

    // 내 예약현황 조회
    @Override
    public List<SeatReservation> findReservationsByMemberId(Integer memberId) {
        String sql = "SELECT sr.reservation_id, sr.seat_id, sr.reservation_time, s.seat_status " +
                "FROM SeatReservation sr " +
                "JOIN Seat s ON sr.seat_id = s.seat_id " +
                "WHERE sr.member_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            SeatReservation reservation = new SeatReservation();
            reservation.setReservationId(rs.getInt("reservation_id"));
            reservation.setSeatId(rs.getInt("seat_id"));
            reservation.setReservationTime(rs.getDate("reservation_time").toLocalDate());
            reservation.setSeatStatus(rs.getString("seat_status"));
            return reservation;
        }, memberId);
    }

    //내 예약 취소하기
    @Override
    public void deleteReservation(int reservationId) {
        System.out.println(reservationId);
        String sql = "DELETE FROM SeatReservation WHERE reservation_id = ?";
        jdbcTemplate.update(sql, reservationId);
    }

}

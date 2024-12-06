package bookDB.demo.Repository;

import bookDB.demo.Domain.Seat;
import bookDB.demo.Domain.SeatReservation;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface SeatRepository {
    public List<Seat> findAllSeats();
    public Seat checkSeatStatus(Long seatId);
    public int reserveSeat(Integer memberId, Integer seatId);
    public List<SeatReservation> findReservationsByMemberId(Integer memberId);
    public void deleteReservation(int reservationId);
}

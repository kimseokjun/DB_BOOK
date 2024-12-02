package bookDB.demo.Repository;

import bookDB.demo.Domain.Seat;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface SeatRepository {
    public List<Seat> findAllSeats();
    public Seat checkSeatStatus(Long seatId);
    public int reserveSeat(Integer memberId, Integer seatId);
}

package bookDB.demo.Service;

import bookDB.demo.Domain.Seat;
import bookDB.demo.Repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    // 모든 좌석 정보 조회
    public List<Seat> getAllSeats() {
        return seatRepository.findAllSeats();
    }

    // 특정 좌석 상태 조회
    public Seat getSeatStatus(Long seatId) {
        return seatRepository.checkSeatStatus(seatId);
    }
}
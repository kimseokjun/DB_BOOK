package bookDB.demo.Controller;

import bookDB.demo.Domain.Seat;
import bookDB.demo.Service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/seats")
public class SeatController {


    private final SeatService seatService;

    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // 좌석 현황 페이지 렌더링
    @GetMapping
    public String showSeats(Model model) {
        List<Seat> seats = seatService.getAllSeats(); // 서비스 계층 통해 좌석 조회
        model.addAttribute("seats", seats);
        return "seats/seats"; // templates/seats.html 렌더링
    }

    // 좌석 상태 조회
    @PostMapping("/checkStatus")
    public String checkSeatStatus(@RequestParam("seatId") Long seatId, Model model) {
        Seat seatStatus = seatService.getSeatStatus(seatId); // 서비스 계층 통해 프로시저 호출
        model.addAttribute("seatStatus", seatStatus);
        return "seats/seatStatus"; // templates/seatStatus.html 렌더링
    }

    // 좌석 예약 처리
    @PostMapping("/reserve")
    public String reserveSeat(@RequestParam("seatId") Integer seatId) {
        Integer memberId = 3; // 임시로 설정된 회원 ID, 로그인 기능 추가 시 수정 필요
        int result = seatService.reserveSeat(memberId, seatId);

        if (result == 1) {
            System.out.println("예약 성공: 좌석 " + seatId + "이(가) 예약되었습니다.");
        } else if (result == 0) {
            System.out.println("예약 실패: 좌석 " + seatId + "은(는) 이미 사용 중입니다.");
        } else {
            System.out.println("예약 실패: 좌석 " + seatId + " 정보를 찾을 수 없습니다.");
        }

        return "redirect:/seats"; // 예약 후 좌석 목록으로 리다이렉트
    }
}

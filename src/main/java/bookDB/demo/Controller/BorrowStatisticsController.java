package bookDB.demo.Controller;

import bookDB.demo.Service.BorrowStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BorrowStatisticsController {

    private final BorrowStatisticsService borrowStatisticsService;

    @Autowired
    public BorrowStatisticsController(BorrowStatisticsService borrowStatisticsService) {
        this.borrowStatisticsService = borrowStatisticsService;
    }

    @PostMapping("/statistics")
    public String getBorrowStatistics(@RequestParam("startDate") String startDate,
                                      @RequestParam("endDate") String endDate,
                                      Model model) {
        // 통계 데이터를 서비스에서 조회
        var statistics = borrowStatisticsService.getBorrowStatistics(startDate, endDate);

        model.addAttribute("statistics", statistics); // 모델에 통계 데이터 추가
        return "borrowStatistics"; // 조회 결과와 함께 HTML 반환
    }

    @GetMapping("/statistics")
    public String showStatisticsForm() {
        return "borrowStatistics"; // 통계 조회 페이지로 이동
    }

}

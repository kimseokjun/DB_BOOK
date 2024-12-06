package bookDB.demo.Controller;


import bookDB.demo.Domain.Borrow;
import bookDB.demo.Domain.SeatReservation;
import bookDB.demo.Service.BorrowReturnService;
import bookDB.demo.Service.SeatService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BorrowReturnController {

    @Autowired
    private final BorrowReturnService borrowReturnService;
    private final SeatService seatService;

    public BorrowReturnController(BorrowReturnService borrowReturnService, SeatService seatService) {
        this.borrowReturnService = borrowReturnService;
        this.seatService = seatService;
    }

        // 대출 기록 목록 페이지
    @GetMapping("/borrows")
    public String listBorrows(Model model) {
        List<Borrow> borrows = borrowReturnService.getAllBorrows();
        System.out.println("Total books : "+ borrows.size());
        System.out.println("컨트롤러" + borrows);
        model.addAttribute("borrows", borrows);
        return "borrows"; // templates/borrows.html
    }

    @GetMapping("/my-records")
    public String viewMyRecords(HttpSession session, Model model) {
        Integer loggedInUser = (Integer) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            List<Borrow> borrows = borrowReturnService.getBorrowRecordsForMember(loggedInUser);
            List<SeatReservation> reservations = seatService.getReservationsByMemberId(loggedInUser);

            model.addAttribute("borrows", borrows);
            model.addAttribute("reservations", reservations);

        } else {
            model.addAttribute("error", "로그인이 필요합니다.");
        }

        return "myRecords";
    }


    @PostMapping("/books/return")
    public String returnBook(@RequestParam Long borrowId) {
        borrowReturnService.returnBook(borrowId);  // 반납 프로시저 실행
        return "redirect:/my-records";  // 대출 목록 페이지로 리다이렉트
    }

    @PostMapping("/borrows/extend")
    public String extendBorrow(@RequestParam("borrowId") int borrowId, Model model) {
        try {
            // 연장 프로시저 실행
            borrowReturnService.extendBorrow(borrowId);
            model.addAttribute("message", "대출이 성공적으로 연장되었습니다.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
        }

        // 연장 후 대출 기록 페이지로 리다이렉트
        return "redirect:/my-records";
    }
}

package bookDB.demo.Controller;


import bookDB.demo.Domain.Borrow;
import bookDB.demo.Service.BorrowReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BorrowReturnController {

    @Autowired
    private final BorrowReturnService borrowReturnService;

    public BorrowReturnController(BorrowReturnService borrowReturnService) {
        this.borrowReturnService = borrowReturnService;
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

//    // 반납 처리
//    @PostMapping("/return")
//    public String returnBorrow(@RequestParam("borrowId") Long borrowId) {
//        borrowService.returnBorrow(borrowId);
//        return "redirect:/borrows";
//    }
}

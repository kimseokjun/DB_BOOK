package bookDB.demo.Controller;

import bookDB.demo.Service.BookRequestService;
import bookDB.demo.Service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class BookRequestController {

    @Autowired
    private final BookRequestService bookRequestService;

    public BookRequestController(BookRequestService bookRequestService) {
        this.bookRequestService = bookRequestService;
    }
    @GetMapping("/bookrequest")
    public String showBookRequestForm() {
        return "requestBook"; // 도서 신청 HTML 반환
    }
    @PostMapping("/bookrequest/add")
    public String addBookRequest(@RequestParam("bookTitle") String bookTitle,
                                 @RequestParam("author") String author,
                                 HttpServletRequest request,
                                 Model model) {
        // 세션에서 현재 로그인된 사용자 ID 가져오기
        HttpSession session = request.getSession();
        Integer loggedInUser = (Integer) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "auth/login"; // 로그인 페이지로 리다이렉트
        }

        // 도서 신청 로직 실행
        try {
            bookRequestService.addBookRequest(bookTitle, author, loggedInUser);
            model.addAttribute("message", "도서 신청이 완료되었습니다.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/bookrequest"; // 도서 신청 목록 페이지로 리다이렉트
    }

}

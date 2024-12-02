package bookDB.demo.Controller;

import bookDB.demo.Service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@RequestMapping("/auth")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "member/login"; // 로그인 폼 렌더링
    }

    @PostMapping("/login")
    public String login(@RequestParam("memberId") int memberId,
                        @RequestParam("password") String password,
                        HttpServletRequest request,
                        Model model) {
        // 로그인 처리 로직
        boolean isAuthenticated = memberService.authenticate(memberId, password);

        if (isAuthenticated) {
            System.out.println("컨트롤러 성공");
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", memberId);
            return "redirect:/home"; // 로그인 성공 시 홈 페이지로 이동
        } else {
            System.out.println("컨트롤러 실패");
            model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "member/login"; // 로그인 실패 시 로그인 폼으로 돌아감
        }
    }


}
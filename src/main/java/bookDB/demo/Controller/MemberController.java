package bookDB.demo.Controller;

import bookDB.demo.Domain.Member;
import bookDB.demo.Service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/register")
    public String registerpage() {
        return "member/register"; // 로그인 폼 렌더링
    }
    @PostMapping("/register")
    public String registerMember(@ModelAttribute Member form) {
        Member member = new Member();
        member.setMemberId(form.getMemberId());
        member.setPassword(form.getPassword());
        member.setMemberName(form.getMemberName());
        member.setEmail(form.getEmail());
        member.setGender(form.getGender());
        System.out.println(member);
        memberService.registerMember(member);
        return "redirect:/";
    }
    @PostMapping("/login")
    public String login(@RequestParam("memberId") int memberId,
                        @RequestParam("password") String password,
                        HttpServletRequest request,
                        Model model) {
        // 로그인 처리 로직
        boolean isAuthenticated = memberService.authenticate(memberId, password);

        if (isAuthenticated) {
            System.out.println("Login successful, member: " + memberId);
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", memberId);
            return "redirect:/home"; // 로그인 성공 시 홈 페이지로 이동
        } else {

            model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "member/login"; // 로그인 실패 시 로그인 폼으로 돌아감
        }
    }
    @GetMapping("/memberInfo")
    public String memberInfo(HttpSession session, Model model) {
        Integer loggedInUserId = (Integer) session.getAttribute("loggedInUser"); // 세션에서 사용자 ID 가져오기
        System.out.println("Session loginMember: " + loggedInUserId);
        if (loggedInUserId == null) {
            return "redirect:/auth/login"; // 로그인 페이지로 리다이렉트
        }

        Member member = memberService.findMemberById(loggedInUserId); // ID로 회원 정보 조회
        if (member == null) {
            return "redirect:/auth/login"; // 회원 정보 없으면 로그인 페이지로 리다이렉트
        }

        model.addAttribute("member", member); // 회원 정보 모델에 추가
        return "member/memberInfo"; // HTML 뷰 렌더링
    }

    @GetMapping("/deleteAccount")
    public String deleteAccountForm(HttpSession session, Model model) {
        Integer loggedInUserId = (Integer) session.getAttribute("loggedInUser");
        if (loggedInUserId == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("member", loggedInUserId);
        return "member/deleteAccount";
    }
    @PostMapping("/deleteAccount")
    public String deleteAccount(HttpSession session, Model model) {
        Integer loggedInUserId = (Integer) session.getAttribute("loggedInUser");
        if (loggedInUserId != null) {
            try {
                memberService.deleteMember(loggedInUserId);
                session.invalidate();
                return "redirect:/";
            } catch (DataIntegrityViolationException e) {
                model.addAttribute("error", "대출 중이거나 예약 중인 경우 탈퇴할 수 없습니다.");
                return "member/deleteAccount";
            }
        }
        return "redirect:/";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }


}
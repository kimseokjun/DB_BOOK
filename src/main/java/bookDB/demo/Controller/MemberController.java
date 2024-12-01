package bookDB.demo.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {
    @GetMapping("/login")
    public String loginPage() {
        return "/member/login"; // templates/login.html 파일 렌더링
    }
    @GetMapping("/register")
    public String registerForm() {
        return "/member/register";  // templates/register.html 렌더링
    }
}

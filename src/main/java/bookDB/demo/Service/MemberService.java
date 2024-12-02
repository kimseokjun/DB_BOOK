package bookDB.demo.Service;


import bookDB.demo.Repository.MemberRepopsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepopsitory memberRepository;

    @Autowired
    public MemberService(MemberRepopsitory memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean authenticate(int memberId, String password) {
        // 데이터베이스에서 ID와 비밀번호 확인
        System.out.println(memberId) ;
        return memberRepository.findByIdAndPassword(memberId, password);
    }

}

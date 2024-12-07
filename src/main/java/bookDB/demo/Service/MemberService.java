package bookDB.demo.Service;


import bookDB.demo.Domain.Member;
import bookDB.demo.Repository.MemberRepopsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

        return memberRepository.findByIdAndPassword(memberId, password);
    }
    public void registerMember(Member member) {
        if (memberRepository.existsById(member.getMemberId())) {
            throw new IllegalArgumentException("이미 존재하는 회원 ID입니다.");
        }
        memberRepository.saveMember(member);
    }


    public void deleteMember(Integer id) {
        try {
            memberRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw e;
        }
    }

    public Member findMemberById(Integer id) {
        return memberRepository.findById(id);
    }

}

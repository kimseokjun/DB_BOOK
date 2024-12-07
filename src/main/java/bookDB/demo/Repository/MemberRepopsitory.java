package bookDB.demo.Repository;

import bookDB.demo.Domain.Member;

public interface MemberRepopsitory {
    public boolean findByIdAndPassword(int memberId, String password);
    public void saveMember(Member member);
    public boolean existsById(int memberId);
    public void deleteById(int id);

    public Member findById(int id);
}

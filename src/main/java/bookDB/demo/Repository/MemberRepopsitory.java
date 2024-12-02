package bookDB.demo.Repository;

public interface MemberRepopsitory {
    public boolean findByIdAndPassword(int memberId, String password);
}

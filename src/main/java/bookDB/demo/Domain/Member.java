package bookDB.demo.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Member {

    @Id
    private Integer memberId;       // 회원 ID (Primary Key)
    private String password;        // 비밀번호
    private String memberName;      // 회원 이름
    private String email;           // 이메일
    private String gender;          // 성별
    private String role;            // 역할 (일반회원, 관리자 등)
    private Integer overdueFee;     // 연체료
    private Integer borrowCount;    // 대출 횟수
    private String membershipLevel; // 회원 등급 (Bronze, Silver, Gold, VIP)

    // 기본 생성자
    public Member() {}

    // 생성자
    public Member(Integer memberId, String password, String memberName, String email, String gender, String role,
                  Integer overdueFee, Integer borrowCount, String membershipLevel) {
        this.memberId = memberId;
        this.password = password;
        this.memberName = memberName;
        this.email = email;
        this.gender = gender;
        this.role = role;
        this.overdueFee = overdueFee;
        this.borrowCount = borrowCount;
        this.membershipLevel = membershipLevel;
    }

    // Getter와 Setter
    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getOverdueFee() {
        return overdueFee;
    }

    public void setOverdueFee(Integer overdueFee) {
        this.overdueFee = overdueFee;
    }

    public Integer getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(Integer borrowCount) {
        this.borrowCount = borrowCount;
    }

    public String getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(String membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    // toString() 메서드 (디버깅 및 로깅 용도)
    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", password='" + password + '\'' +
                ", memberName='" + memberName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", role='" + role + '\'' +
                ", overdueFee=" + overdueFee +
                ", borrowCount=" + borrowCount +
                ", membershipLevel='" + membershipLevel + '\'' +
                '}';
    }
}

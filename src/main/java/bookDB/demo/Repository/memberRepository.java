package bookDB.demo.Repository;

import bookDB.demo.Domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class memberRepository implements  MemberRepopsitory{

    private final JdbcTemplate jdbcTemplate;

    public memberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean findByIdAndPassword(int memberId, String password) {
        String sql = "SELECT COUNT(*) FROM Members WHERE member_id = ? AND password = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{memberId, password}, Integer.class);
        return count != null && count > 0; // ID와 비밀번호가 일치하면 true 반환
    }

    @Override
    public void saveMember(Member member) {
        String sql = "INSERT INTO Members (member_id, password, member_name, email, gender, membership_level) " +
                "VALUES (?, ?, ?, ?, ?, DEFAULT)";
        jdbcTemplate.update(sql,
                member.getMemberId(),
                member.getPassword(),
                member.getMemberName(),
                member.getEmail(),
                member.getGender());
    }

    @Override
    public boolean existsById(int memberId) {
        String sql = "SELECT COUNT(*) FROM Members WHERE member_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId);
        return count != null && count > 0;
    }

    @Override
    public void deleteById(int id) {
        System.out.println(id);
        String sql = "DELETE FROM MEMBERS WHERE MEMBER_ID = ?";
        int rowsAffected = jdbcTemplate.update(sql, id); // SQL 실행
        if (rowsAffected > 0) {
            System.out.println("Member with ID " + id + " was deleted successfully.");
        } else {
            System.out.println("No member found with ID " + id);
        }
    }

    @Override
    public Member findById(int id) {
        String sql = "SELECT * FROM MEMBERS WHERE MEMBER_ID = ?";
        List<Member> result = jdbcTemplate.query(sql, new Object[]{id}, memberRowMapper());
        return result.isEmpty() ? null : result.get(0);
    }
    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getInt("MEMBER_ID"));
            member.setMemberName(rs.getString("MEMBER_NAME"));
            member.setEmail(rs.getString("EMAIL"));
            member.setGender(rs.getString("GENDER"));
            member.setRole(rs.getString("ROLE"));
            member.setMembershipLevel(rs.getString("membership_level"));
            member.setOverdueFee(rs.getInt("OVERDUE_FEE"));
            member.setPassword(rs.getString("PASSWORD"));

            return member;
        };
    }
}

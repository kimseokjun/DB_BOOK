package bookDB.demo.Repository;

import bookDB.demo.Domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}

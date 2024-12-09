package bookDB.demo.Repository;

import bookDB.demo.Domain.Borrow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class borrowNReturnRepository implements BorrowReturnRepository{

    private static final Logger logger = LoggerFactory.getLogger(borrowNReturnRepository.class);
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public borrowNReturnRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public List<Borrow> findAllBorrows() {
        String sql = "SELECT * FROM Borrow";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Borrow borrow = new Borrow();
            borrow.setBorrowId(rs.getInt("borrow_id"));  // Integer로 설정
            borrow.setIsbn(rs.getString("isbn"));
            borrow.setMemberId(rs.getInt("member_id"));  // Integer로 설정
            borrow.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
            borrow.setDueDate(rs.getDate("due_date").toLocalDate());
            borrow.setReturnDate(rs.getDate("return_date") != null
                    ? rs.getDate("return_date").toLocalDate()
                    : null);
            borrow.setReturnStatus(rs.getString("return_status"));
            return borrow;
        });
    }
//     jdbcTemplate.execute((Connection connection) -> {
//        try (CallableStatement callableStatement = connection.prepareCall(sql)) {
//            callableStatement.setInt(1, borrowId);
//            callableStatement.execute();
//        }
//        return null; // execute 메서드는 결과가 필요 없으므로 null 반환
//    });
    //도서 반납 프로시저
    @Override
    public void returnBook(int borrowId) {
        String sql = "{call RETURN_BOOK(?)}"; // 프로시저 호출 SQL
        jdbcTemplate.execute((Connection connection) -> {
            try (CallableStatement callableStatement = connection.prepareCall(sql)) {
                // 매개변수 설정
                callableStatement.setLong(1, borrowId);

                // 프로시저 실행
                callableStatement.execute();
                return null; // execute는 결과를 반환하지 않음
            }
        });
    }

    // 내 대출 목록
    @Override
    public List<Borrow> findByMemberId(int memberId) {
        String sql = "SELECT * FROM Borrow WHERE member_id = ?";

        // RowMapper를 사용해 결과를 Borrow 객체로 매핑
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            Borrow borrow = new Borrow();
            borrow.setBorrowId(rs.getInt("borrow_id"));
            borrow.setIsbn(rs.getString("isbn"));
            borrow.setMemberId(rs.getInt("member_id"));
            borrow.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
            borrow.setDueDate(rs.getDate("due_date").toLocalDate());
            borrow.setReturnDate(rs.getDate("return_date") != null
                    ? rs.getDate("return_date").toLocalDate()
                    : null);
            borrow.setReturnStatus(rs.getString("return_status"));
            borrow.setIsExtended(rs.getInt("is_extended"));
            return borrow;
        });

    }

    @Override
    public void extendBorrow(int borrowId) {
        String sql = "{ call extend_borrow(?) }";
        jdbcTemplate.execute((Connection connection) -> {
            try (CallableStatement callableStatement = connection.prepareCall(sql)) {
                callableStatement.setInt(1, borrowId);
                callableStatement.execute();
            }
            return null; // execute 메서드는 결과가 필요 없으므로 null 반환
        });
    }

}

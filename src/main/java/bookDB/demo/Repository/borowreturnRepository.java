package bookDB.demo.Repository;

import bookDB.demo.Domain.Borrow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class borowreturnRepository implements BorrowReturnRepository{

    private static final Logger logger = LoggerFactory.getLogger(borowreturnRepository.class);
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Autowired
    public borowreturnRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    @Override
    public List<Borrow> findAllBorrows() {
        String sql = "SELECT * FROM Borrow";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            logger.info("Database connection established");

            pstmt = conn.prepareStatement(sql);
            logger.info("PreparedStatement created");

            rs = pstmt.executeQuery();
            logger.info("Query executed: {}", sql);

            List<Borrow> borrows = new ArrayList<>();
            while (rs.next()) {
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

                borrows.add(borrow);
                System.out.println("Borrow added: " + borrow);
                System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
            }
            return borrows;
        } catch (Exception e) {
            logger.error("Error fetching all borrow records", e);
            throw new IllegalStateException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                logger.error("Error closing resources", e);
            }
        }
    }

    @Override
    public void returnBook(Long borrowId) {
        String sql = "{call RETURN_BOOK(?)}";  // 저장 프로시저 호출
        jdbcTemplate.update(sql, borrowId);  // 대출번호를 인자로 전달
    }
}

package bookDB.demo.Repository;

import bookDB.demo.Domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class jdbcBookRepository implements BookRepository {

    private static final Logger logger = LoggerFactory.getLogger(jdbcBookRepository.class);

    private final DataSource dataSource;

    @Autowired
    public jdbcBookRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Book> findALl() {
        String sql = "SELECT * FROM BOOKS";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            logger.info("Database connection established"); // Connection 확인
            pstmt = conn.prepareStatement(sql);
            logger.info("PreparedStatement created"); // PreparedStatement 확인

            rs = pstmt.executeQuery();
            logger.info("Query executed: {}", sql); // Query 실행 확인

            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                Book book = new Book();
                book.setISBN(rs.getString("ISBN"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));
                book.setGenre(rs.getString("genre"));
                book.setIsBorrowed(rs.getInt("is_borrowed"));
                book.setBorrowCount(rs.getInt("borrow_count"));
                book.setLocation(rs.getString("location"));
             //   book.setRegistrationDate(rs.getDate("registration_date").toString());


                books.add(book);
                System.out.println("Book added:"+ book); // 데이터 확인
            }
            return books;
        } catch (Exception e) {
            logger.error("Error fetching books", e); // 예외 로그 출력
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
}

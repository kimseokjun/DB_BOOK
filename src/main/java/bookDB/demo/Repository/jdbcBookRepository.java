package bookDB.demo.Repository;

import bookDB.demo.Domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class jdbcBookRepository implements BookRepository {

    private static final Logger logger = LoggerFactory.getLogger(jdbcBookRepository.class);

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public jdbcBookRepository(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
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

    @Override
    public Book addBook(Book book) {
        String sql = "INSERT INTO Books (isbn, title, author, publisher, genre, is_borrowed, borrow_count, location) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = dataSource.getConnection(); // 데이터소스를 통해 연결
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, book.getISBN());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublisher());
            pstmt.setString(5, book.getGenre());
            pstmt.setInt(6, book.getIsBorrowed()); // 초기값: 0
            pstmt.setInt(7, book.getBorrowCount()); // 초기값: 0
            pstmt.setString(8, book.getLocation());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 1) {
                return book; // 삽입 성공
            } else {
                throw new SQLException("Book insertion failed");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to add book", e);
        } finally {
            close(conn, pstmt, null); // 리소스 정리
        }
    }

    @Override
    public void deleteById(String isbn) {
        System.out.println(isbn);
        String sql = "DELETE FROM Books WHERE ISBN = ?";  // ISBN을 기준으로 삭제
        int rowsAffected = jdbcTemplate.update(sql, isbn); // SQL 실행
        if (rowsAffected > 0) {
            System.out.println("Book with ISBN " + isbn + " was deleted successfully.");
        } else {
            System.out.println("No book found with ISBN " + isbn);
        }
    }

    @Override
    public List<Book> findAllByOrderByBorrowCountDesc() {
        String sql = "SELECT * FROM Books ORDER BY borrow_count DESC"; // 대출 수 많은 순으로 정렬
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Book book = new Book();
            book.setISBN(rs.getString("ISBN"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));
            book.setPublisher(rs.getString("publisher"));
            book.setGenre(rs.getString("genre"));
            book.setIsBorrowed(rs.getInt("is_borrowed"));
            book.setBorrowCount(rs.getInt("borrow_count"));
            book.setLocation(rs.getString("location"));
            return book;
        });
    }

    @Override
    public List<Book> findByGenreOrderByBorrowCountDesc(String genre) {
        String sql = "SELECT * FROM Books WHERE genre = ? ORDER BY borrow_count DESC"; // 장르별 대출 수 많은 순으로 정렬
        return jdbcTemplate.query(sql, new Object[]{genre}, (rs, rowNum) -> {
            Book book = new Book();
            book.setISBN(rs.getString("ISBN"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));
            book.setPublisher(rs.getString("publisher"));
            book.setGenre(rs.getString("genre"));
            book.setIsBorrowed(rs.getInt("is_borrowed"));
            book.setBorrowCount(rs.getInt("borrow_count"));
            book.setLocation(rs.getString("location"));
            return book;
        });
    }

    @Override
    public List<String> findAllGenres() {
        String sql = "SELECT DISTINCT genre FROM Books"; // 모든 고유 장르 목록 조회
        return jdbcTemplate.queryForList(sql, String.class); // 장르만 리스트로 반환;
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

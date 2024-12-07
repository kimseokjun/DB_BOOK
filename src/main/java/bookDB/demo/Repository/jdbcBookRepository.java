package bookDB.demo.Repository;

import bookDB.demo.Domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class jdbcBookRepository implements BookRepository {

    private static final Logger logger = LoggerFactory.getLogger(jdbcBookRepository.class);


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public jdbcBookRepository(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> findALl() {
        String sql = "SELECT * FROM BOOKS";

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
            // book.setRegistrationDate(rs.getDate("registration_date").toString());

            logger.info("Book added: {}", book); // 데이터 확인 로그
            return book;
        });
    }
    @Override
    public Book addBook(Book book) {
        String sql = "INSERT INTO Books (isbn, title, author, publisher, genre, is_borrowed, borrow_count, location) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(
                sql,
                book.getISBN(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getGenre(),
                book.getIsBorrowed(), // 초기값: 0
                book.getBorrowCount(), // 초기값: 0
                book.getLocation()
        );

        if (rowsAffected == 1) {
            return book; // 삽입 성공
        } else {
            throw new IllegalStateException("Book insertion failed");
        }
    }

    @Override
    public void deleteById(String isbn) {
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
            System.out.println(book.getBorrowCount());
            return book;
        });
    }

    @Override
    public List<String> findAllGenres() {
        String sql = "SELECT DISTINCT genre FROM Books"; // 모든 고유 장르 목록 조회
        return jdbcTemplate.queryForList(sql, String.class); // 장르만 리스트로 반환;
    }

    @Override
    public List<Book> findAvailableBooks() {
        String sql = "SELECT * FROM Books WHERE is_borrowed = 0";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Book book = new Book();
            book.setISBN(rs.getString("ISBN"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));
            book.setPublisher(rs.getString("publisher"));
            book.setGenre(rs.getString("genre"));
            return book;
        });
    }

    @Override
    public String executeBorrowProcedure(int memberId, String isbn) {
        String sql = "{call BORROW_BOOK(?, ?, ?)}";
        return jdbcTemplate.execute(sql, (CallableStatement cs) -> {
            cs.setInt(1, memberId);
            cs.setString(2, isbn);
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.execute();
            return cs.getString(3); // 결과 메시지 반환
        });
    }
    @Override
    public List<Book> searchBooks(String title, String genre) {
        String sql;
        Object[] params;
        if ((title != null && !title.isEmpty()) && (genre != null && !genre.isEmpty())) {
            sql = "SELECT * FROM BOOKS WHERE TITLE LIKE ? AND GENRE = ?";
            params = new Object[]{"%" + title + "%", genre};
        } else if (title != null && !title.isEmpty()) {
            sql = "SELECT * FROM BOOKS WHERE TITLE LIKE ?";
            params = new Object[]{"%" + title + "%"};
        } else if (genre != null && !genre.isEmpty()) {
            sql = "SELECT * FROM BOOKS WHERE GENRE = ?";
            params = new Object[]{genre};
        } else {
            return List.of();
        }
        return jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            Book book = new Book();
            book.setISBN(rs.getString("ISBN"));
            book.setTitle(rs.getString("TITLE"));
            book.setAuthor(rs.getString("AUTHOR"));
            book.setPublisher(rs.getString("PUBLISHER"));
            book.setGenre(rs.getString("GENRE"));
            book.setIsBorrowed(rs.getInt("IS_BORROWED"));
            book.setBorrowCount(rs.getInt("BORROW_COUNT"));
            book.setLocation(rs.getString("LOCATION"));
            return book;
        });
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

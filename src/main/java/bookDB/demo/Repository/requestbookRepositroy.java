package bookDB.demo.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class requestbookRepositroy implements BookRequestRepository{

    private final JdbcTemplate jdbcTemplate;

    public requestbookRepositroy(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public void addBookRequest(String bookTitle, String author, Integer memberId) {
        String sql = "INSERT INTO BOOKREQUEST (BOOK_TITLE, AUTHOR, MEMBER_ID) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, bookTitle, author, memberId);
    }

}

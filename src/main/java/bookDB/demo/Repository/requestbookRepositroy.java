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
        String checkSql = "SELECT REQUEST_COUNT FROM BOOKREQUEST WHERE BOOK_TITLE = ?";
        Integer currentCount = null;
        try {
            currentCount = jdbcTemplate.queryForObject(checkSql, new Object[]{bookTitle}, Integer.class);
        } catch (Exception e) {
            System.out.println("해당 도서는 신청된 적이 없습니다");
        }
        if (currentCount != null) {
            String updateSql = "UPDATE BOOKREQUEST SET REQUEST_COUNT = REQUEST_COUNT + 1 WHERE BOOK_TITLE = ?";
            jdbcTemplate.update(updateSql, bookTitle);
            System.out.println("도서 신청 횟수 증가");
        } else {
            
            String insertSql = "INSERT INTO BOOKREQUEST (REQUEST_ID, BOOK_TITLE, AUTHOR, MEMBER_ID, REQUEST_COUNT) " +
                    "VALUES (SEQ_REQUEST_ID.NEXTVAL, ?, ?, ?, 1)";
            jdbcTemplate.update(insertSql, bookTitle, author, memberId);
            System.out.println("도서 신청 완료");
        }
    }

}

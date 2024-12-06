package bookDB.demo.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class BookRequest {

    @Id
    private Integer requestId;
    private String bookTitle;
    private String author;
    private Integer memberId; // 요청한 회원의 ID

    // Getters and Setters
    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "BookRequest{" +
                "requestId=" + requestId +
                ", bookTitle='" + bookTitle + '\'' +
                ", author='" + author + '\'' +
                ", memberId=" + memberId +
                '}';
    }
}

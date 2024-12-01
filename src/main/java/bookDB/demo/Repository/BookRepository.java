package bookDB.demo.Repository;

import bookDB.demo.Domain.Book;
import bookDB.demo.Domain.Borrow;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository {

    List<Book> findALl(); //모든 도서 보는거

    Book addBook(Book book);

    public void deleteById(String isbn);

    // 대출수 많은 순으로 모든 책 조회
    List<Book> findAllByOrderByBorrowCountDesc();

    // 장르별로 대출수 많은 순으로 책 조회
    List<Book> findByGenreOrderByBorrowCountDesc(String genre);

    // 모든 장르 목록 조회
    @Query("SELECT DISTINCT b.genre FROM Book b")
    List<String> findAllGenres();
}



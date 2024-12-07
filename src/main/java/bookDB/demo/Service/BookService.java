package bookDB.demo.Service;

import bookDB.demo.Domain.Book;
import bookDB.demo.Repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findBooks() {
        return bookRepository.findALl();
    }

    public void addBook(Book book) {
        if (book.getISBN() == null || book.getISBN().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        bookRepository.addBook(book);
    }

    public void deleteBook(String isbn) {

        bookRepository.deleteById(isbn); // ISBN으로 책 삭제
    }

    // 모든 책을 대출수 많은 순으로 가져오기
    public List<Book> findAllBooksSortedByBorrowCount() {
        return bookRepository.findAllByOrderByBorrowCountDesc(); // 대출 수가 많은 순으로 정렬
    }

    // 장르별로 대출수 많은 순으로 책 가져오기
    public List<Book> findBooksByGenreSortedByBorrowCount(String genre) {
        return bookRepository.findByGenreOrderByBorrowCountDesc(genre); // 장르별로 대출 수가 많은 순으로 정렬
    }

    // 모든 장르 목록 가져오기
    public List<String> findAllGenres() {
        return bookRepository.findAllGenres(); // 책 테이블에서 모든 장르를 가져옴
    }
    // 대출 가능한 도서 목록 가져오기
    public List<Book> getAvailableBooks() {
        return bookRepository.findAvailableBooks();
    }

    //대출 기능
    public String borrowBook(int memberId, String isbn) {
        return bookRepository.executeBorrowProcedure(memberId, isbn);
    }

    public List<Book> searchBooks(String title, String genre) {
        return bookRepository.searchBooks(title, genre);
    }
}

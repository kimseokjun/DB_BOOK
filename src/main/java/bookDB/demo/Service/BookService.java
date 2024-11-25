package bookDB.demo.Service;

import bookDB.demo.Domain.Book;
import bookDB.demo.Repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findBooks() {
        return bookRepository.findALl();
    }
}

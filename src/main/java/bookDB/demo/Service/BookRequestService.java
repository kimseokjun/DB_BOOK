package bookDB.demo.Service;

import bookDB.demo.Domain.BookRequest;
import bookDB.demo.Repository.BookRepository;
import bookDB.demo.Repository.BookRequestRepository;
import org.springframework.stereotype.Service;

@Service
public class BookRequestService {

    private final BookRequestRepository bookRequestRepository;

    public BookRequestService(BookRequestRepository bookRequestRepository) {
        this.bookRequestRepository = bookRequestRepository;
    }

    public void addBookRequest(String bookTitle, String author, Integer memberId) {
        // 레포지토리 호출
        bookRequestRepository.addBookRequest(bookTitle, author, memberId);
    }

}

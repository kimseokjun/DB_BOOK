package bookDB.demo.Repository;

import bookDB.demo.Domain.Book;
import bookDB.demo.Domain.Borrow;

import java.util.List;

public interface BookRepository {

    List<Book> findALl(); //모든 도서 보는거

    Book addBook(Book book);

    public void deleteById(String isbn);
}

package bookDB.demo.Repository;

import bookDB.demo.Domain.Book;

import java.util.List;

public interface BookRepository {

    List<Book> findALl();
}

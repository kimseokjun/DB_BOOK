package bookDB.demo.Repository;

import bookDB.demo.Domain.Borrow;

import java.util.List;

public interface BorrowReturnRepository {

    public List<Borrow> findAllBorrows();

   public void returnBook(Long borrowId);
}

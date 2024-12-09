package bookDB.demo.Service;

import bookDB.demo.Domain.Book;
import bookDB.demo.Domain.Borrow;
import bookDB.demo.Repository.BorrowReturnRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class BorrowReturnService {

    private final BorrowReturnRepository borrowReturnRepository;

    public BorrowReturnService(BorrowReturnRepository borrowReturnRepository) {
        this.borrowReturnRepository = borrowReturnRepository;
    }


    public List<Borrow> getAllBorrows(){
        return borrowReturnRepository.findAllBorrows();
    }

    public void returnBook(int borrowId) {
        borrowReturnRepository.returnBook(borrowId);  // 저장 프로시저 실행
    }
    public List<Borrow> getBorrowRecordsForMember(int memberId) {
        return borrowReturnRepository.findByMemberId(memberId);
    }
    public void extendBorrow(int borrowId) {
        borrowReturnRepository.extendBorrow(borrowId);
    }
}

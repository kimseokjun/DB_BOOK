package bookDB.demo.Controller;


import bookDB.demo.Domain.Book;
import bookDB.demo.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BookController {
    @Autowired
    private final BookService bookService;


    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }



    @GetMapping("/books")
    public String list(Model model){
        List<Book> books = bookService.findBooks();
        System.out.println("Total books : "+ books.size());
        model.addAttribute("books",books);
        return "books/bookList";
    }
    // 책 추가 폼 표시
    @GetMapping("/books/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book()); // 빈 Book 객체를 모델에 추가
        return "books/addBookForm"; // addBookForm.html 파일 이름
    }


    // 책 추가 처리
    @PostMapping("/books/add")
    public String addBook(Book form) {
        Book book = new Book();
        book.setISBN(form.getISBN());
        book.setTitle(form.getTitle());
        book.setAuthor(form.getAuthor());
        book.setPublisher(form.getPublisher());
        book.setGenre(form.getGenre());
        book.setIsBorrowed(0); // 초기값: 미대출 상태
        book.setBorrowCount(0); // 초기값: 대출 횟수 0
        book.setLocation(form.getLocation());

        bookService.addBook(book); // 서비스 호출하여 저장
        return "redirect:/books"; // 책 목록 페이지로 리다이렉트
    }

    // 책 삭제 처리 메서드
    @PostMapping("/books/delete")
    public String deleteBook(@RequestParam String isbn) {
        bookService.deleteBook(isbn); // isbn으로 책 삭제
        return "redirect:/books"; // 삭제 후 책 목록으로 리다이렉트
    }
}

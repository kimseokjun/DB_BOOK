package bookDB.demo.Controller;


import bookDB.demo.Domain.Book;
import bookDB.demo.Service.BookService;
import jakarta.servlet.http.HttpSession;
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
    public String index(HttpSession session, Model model) {
        Integer loggedInUser = (Integer) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser); // 로그인된 사용자 ID를 모델에 추가
        System.out.println(loggedInUser);

        return "home"; // 메인 페이지로 이동
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
        System.out.println("컨트롤러입니다. 도서 삭제");
        bookService.deleteBook(isbn); // isbn으로 책 삭제
        return "redirect:/books"; // 삭제 후 책 목록으로 리다이렉트
    }
    // 장르별로 대출수 많은 도서 가져오기
    @GetMapping("/books/genre")
    public String showBooksByGenre(@RequestParam(required = false) String genre, Model model) {
        // 장르 목록 가져오기
        List<String> genres = bookService.findAllGenres();
        model.addAttribute("genres", genres);  // 장르 목록을 모델에 추가

        // 선택된 장르를 모델에 추가
        model.addAttribute("selectedGenre", genre); // 선택된 장르를 모델에 추가

        // 장르 선택에 따라 대출수 많은 도서 가져오기
        List<Book> books;
        if (genre == null || genre.isEmpty()) {
            // 장르가 없으면 모든 도서를 대출수 많고 순으로 가져오기
            books = bookService.findAllBooksSortedByBorrowCount();
        } else {
            // 선택된 장르에 해당하는 도서 대출수 많고 순으로 가져오기
            books = bookService.findBooksByGenreSortedByBorrowCount(genre);
        }

        model.addAttribute("books", books);
        return "books/genreBookList"; // 새로운 HTML 페이지로 이동
    }

}

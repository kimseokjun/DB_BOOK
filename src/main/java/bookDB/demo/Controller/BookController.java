package bookDB.demo.Controller;


import bookDB.demo.Domain.Book;
import bookDB.demo.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}

package bookDB.demo.Domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.cglib.core.Local;


import java.time.LocalDate;
import java.util.Date;

@Entity

public class Book {

    @Id
    String ISBN;
    String title;
    String author;
    String publisher;
    String genre;
    Integer is_borrowed;
    Integer borrow_count;
    String location;
  //  LocalDate registration_date;

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and Setter for author
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    // Getter and Setter for publisher
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    // Getter and Setter for genre
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    // Getter and Setter for isBorrowed
    public int getIsBorrowed() {
        return is_borrowed;
    }

    public void setIsBorrowed(Integer isBorrowed) {
        this.is_borrowed = is_borrowed;
    }

    // Getter and Setter for borrowCount
    public int getBorrowCount() {
        return borrow_count;
    }

    public void setBorrowCount(Integer borrowCount) {
        this.borrow_count = borrow_count;
    }

    // Getter and Setter for location
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Getter and Setter for registrationDate
//    public LocalDate getRegistrationDate() {
//        return registration_date;
//    }
//
//    public LocalDate setRegistrationDate(LocalDate registrationDate) {
//        this.registration_date = registration_date;
//    }
}

package bookDB.demo.Repository;


public interface BookRequestRepository {
    public void addBookRequest(String bookTitle, String author, Integer memberId);
}

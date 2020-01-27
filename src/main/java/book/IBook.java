package book;

public interface IBook {

    Book getBook(int id);
    Book getNullBook(int id);
    int getBookId(String title);
    void addBook(String title, String genre, String publisher, String language,String alley, String bookstand, int shelf);
    void removeBook(int id);
    void editBook(int id, String title, String publisher, String genre, String language);
    void editBook(int id, String alley, String bookstand, int shelf);
    void setBookAvailability(int bookId, boolean isAvailable);
    String getMessage();
}
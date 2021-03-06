package bookTransfer;

import java.util.List;

public interface IBookTransfer {

    List<BookTransfer> getReservedUserBooks(int userId);
    List<BookTransfer> getLentUserBooks(int userId);
    List<BookTransfer> getAllLentBooks();
    List<BookTransfer> getAllReservedBooks();
    void reserveBook(int userId, int bookId);
    void lendBook(int userId, int bookId);
    void acceptReturnBook(int bookId);
    void unReserveBook(int bookId);
    String getMessage();
}

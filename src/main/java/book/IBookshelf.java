package book;

public interface IBookshelf {

    int getBookshelf(String alley, String bookstand, int shelf);
    void addBookshelf(String alley, String bookstand, int shelf);
    void removeBookshelf(String alley, String bookstand, int shelf);
    String[] getAlleys();
    String[] getBookstands();
    String[] getShelves();
}

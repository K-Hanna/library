package book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static config.DBConfig.closeDBResources;
import static config.DBConfig.initializeDataBaseConnection;

public class BookService implements IBook {

    private IBookshelf iBookshelf = new BookshelfService();
    private String SQL, message;

    @Override
    public Book getBook(int idBook) {

        Book book = null;
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        SQL = "select * from book inner join bookshelves on bookshelves.bookshelf_id = book.bookshelf_id where book.book_id = ? ;";

        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, idBook);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                book = new Book();
                Bookshelf bookshelf = new Bookshelf(rs.getString("alley"), rs.getString("bookstand"), rs.getInt("shelf"));
                book.setTitle(rs.getString("title"));
                book.setBookshelf(bookshelf);
                book.setPublisher(rs.getString("publisher"));
                book.setLanguage(rs.getString("lang"));
                book.setGenre(rs.getString("genre"));
                book.setISBN(rs.getLong("isbn"));
                book.setBookId(rs.getInt("book_id"));
                book.setAvailable(rs.getBoolean("available"));
            }
            return book;
        } catch (SQLException e) {
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw  new RuntimeException("Error during invoke SQL query");
        }
        finally {
            closeDBResources(connection,preparedStatement);
        }
    }

    @Override
    public Book getNullBook(int idBook) {
        Book book = null;
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        SQL = "select * from book where book_id = ? ;";

        try {
            preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setInt(1, idBook);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                book = new Book();
                Bookshelf bookshelf = new Bookshelf("-", "-", 0);
                book.setTitle(rs.getString("title"));
                book.setBookshelf(bookshelf);
                book.setPublisher(rs.getString("publisher"));
                book.setLanguage(rs.getString("lang"));
                book.setGenre(rs.getString("genre"));
                book.setISBN(rs.getLong("isbn"));
                book.setBookId(rs.getInt("book_id"));
                book.setAvailable(rs.getBoolean("available"));
            }
            return book;
        } catch (SQLException e) {
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw  new RuntimeException("Error during invoke SQL query");
        }
        finally {
            closeDBResources(connection,preparedStatement);
        }
    }

    @Override
    public int getBookId(String title) {
        int bookId = 0;
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        SQL = "select book_id from book where title = ?;";

        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, title);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                bookId = rs.getInt(1);}

            return bookId;

        } catch (SQLException e) {
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw  new RuntimeException("Error during invoke SQL query");
        }
        finally {
            closeDBResources(connection,preparedStatement);
        }
    }

    @Override
    public void addBook(String title, String genre, String publisher,
                        String language, String alley, String bookstand, int shelf) {

        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        SQL = "INSERT INTO book (title, publisher, genre, lang, available, ISBN, bookshelf_id) " +
                " SELECT ?, ?, ?, ?, ?, ?, (SELECT bookshelf_id from bookshelves where alley = ? and bookstand = ? and shelf = ?)" +
                "WHERE NOT EXISTS (SELECT 1 FROM book WHERE title = ?);";

        try {
            preparedStatement = connection.prepareStatement(SQL);

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, publisher);
            preparedStatement.setString(3, genre);
            preparedStatement.setString(4, language);
            preparedStatement.setBoolean(5,true);
            preparedStatement.setLong(6, System.currentTimeMillis());
            preparedStatement.setString(7, alley);
            preparedStatement.setString(8, bookstand);
            preparedStatement.setInt(9, shelf);
            preparedStatement.setString(10, title);

            preparedStatement.executeUpdate();
            message = "Książka została dodana do bazy.";

        } catch (SQLException e) {
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw  new RuntimeException("Error during invoke SQL query");
        }
        finally {
            closeDBResources(connection, preparedStatement);
        }
    }

    @Override
    public void removeBook(int id) {

        SQL = "delete from book where book_id = ?;";
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            message = "Książka została usunięta z bazy.";
        } catch (SQLException e) {
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw  new RuntimeException("Error during invoke SQL query");
        }
        finally {
            closeDBResources(connection,preparedStatement);
        }
    }

    @Override
    public void editBook(int id, String title, String publisher, String genre, String language) {

        SQL = "update book set title = ?, publisher = ?, genre = ?, lang = ? where book.book_id = ?";
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, publisher);
            preparedStatement.setString(3, genre);
            preparedStatement.setString(4, language);
            preparedStatement.setInt(5, id);

            preparedStatement.executeUpdate();
            message = "Książka została zedytowana.";
        } catch (SQLException e) {
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw  new RuntimeException("Error during invoke SQL query");
        }
        finally {
            closeDBResources(connection,preparedStatement);
        }
    }

    @Override
    public void editBook(int id, String alley, String bookstand, int shelf) {

        int bookshelfId = iBookshelf.getBookshelf(alley, bookstand,shelf);
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        try {

            if(bookshelfId > 0) {
                SQL = "update book set bookshelf_id = ? where book.book_id = ?;";
                preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setInt(1, bookshelfId);
                preparedStatement.setInt(2, id);
            } else {
                SQL = "update book set bookshelf_id = null where book.book_id = ?;";
                preparedStatement = connection.prepareStatement(SQL);
                preparedStatement.setInt(1, id);
            }

            preparedStatement.executeUpdate();
            message = "Lokalizacja książki zmieniona na: alejka:" + alley + ", regał: " + bookstand + ", półka: " + shelf + ".";
        } catch (SQLException e) {
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw  new RuntimeException("Error during invoke SQL query");
        }
        finally {
            closeDBResources(connection,preparedStatement);
        }
    }

    @Override
    public void setBookAvailability(int bookId, boolean isAvailable) {
        Connection connection = initializeDataBaseConnection();
        PreparedStatement preparedStatement = null;

        SQL = "update book set available = ? where book.book_id = ?";

        try {
            preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setBoolean(1, isAvailable);
            preparedStatement.setInt(2, bookId);

            preparedStatement.executeUpdate();
            message = "Dostępność książki została zmieniona";
        } catch (SQLException e) {
            System.err.println("Error during invoke SQL query: \n" + e.getMessage());
            throw  new RuntimeException("Error during invoke SQL query");
        }
        finally {
            closeDBResources(connection,preparedStatement);
        }
    }

    @Override
    public String getMessage(){
        return message;
    }
}
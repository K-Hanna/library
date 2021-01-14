package gui.book;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import book.*;
import bookTransfer.BookTransfer;
import bookTransfer.BookTransferService;
import bookTransfer.IBookTransfer;
import gui.general.MyButton;
import reader.IReaderDBService;
import reader.Reader;
import reader.ReaderDBServiceImpl;
import user.IUserDBService;
import user.User;
import user.UserDBServiceImpl;

public class BookGetPanel extends JPanel {

    private JLabel searchByLabel, keyWordLabel, result;
    private JComboBox<String> searchBy;
    private JTextField keyWord;
    private JList<AuthorBook> resultList;
    private MyButton search, create, remove, edit, location, unavailable, lend;
    private JScrollPane scrollPane;
    private String readerCard;

    private IBook iBook = new BookService();
    private IAuthor iAuthor = new AuthorService();
    private IAuthorBook iAuthorBook = new AuthorBookService();
    private IReaderDBService readerDBService = new ReaderDBServiceImpl();
    private IBookTransfer bookTransfer = new BookTransferService();
    private IUserDBService userDBService = new UserDBServiceImpl();
    private IBook bookService = new BookService();

    public BookGetPanel() {

        List<BookTransfer> reservedBooks = bookTransfer.getAllReservedBooks();
        List<BookTransfer> lentBooks = bookTransfer.getAllLentBooks();

        for(BookTransfer book : reservedBooks){
            if(book.getDuedate().before(new Date())){
                bookTransfer.unReserveBook(book.getBook_id());
                bookService.setBookAvailability(book.getBook_id(), true);
            }
        }

        for(BookTransfer book : lentBooks){
            if(book.getDuedate().before(new Date())){
                int userId = readerDBService.getUserId(book.getReader_id());
                User user = userDBService.readUserFromDBById(userId);
                JOptionPane.showMessageDialog(this, book.getAuthorBook() +
                        " jest przetrzymana u: " + user.toString());
            }
        }

        setLayout(null);

        createComps();
        addComps();
        actions();

    }

    private void createBookJList(List<AuthorBook> bookList){

        DefaultListModel<AuthorBook> listModel = new DefaultListModel<>();
        for (AuthorBook aBookList : bookList) {
            listModel.addElement(aBookList);
        }

        resultList.setModel(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void createComps() {

        searchByLabel = new JLabel("Wyszukaj po:");
        searchByLabel.setBounds(20,20,100,30);

        searchBy = new JComboBox<>(new String[]{"tytule", "autorze (imię, nazwisko)", "wydawcy", "gatunku", "języku"});
        searchBy.setBounds(140,20,200,30);

        keyWordLabel = new JLabel("Słowo kluczowe:");
        keyWordLabel.setBounds(20,60,100,30);

        keyWord = new JTextField();
        keyWord.setBounds(140,60,200,30);

        resultList = new JList<>();
        resultList.setBounds(20,100,600,240);

        scrollPane = new JScrollPane(resultList);
        scrollPane.setBounds(20,100,600,240);
        scrollPane.setBorder(new TitledBorder("Wyniki wyszukiwania:"));
        scrollPane.setBackground(Color.white);

        result = new JLabel();
        result.setBounds(20,100,600,240);
        result.setBorder(new TitledBorder("Wyniki wyszukiwania:"));
        result.setBackground(Color.white);
        result.setOpaque(true);
        result.setVerticalAlignment(1);

        search = new MyButton(true);
        search.setText("Szukaj książki");
        search.setBounds(350,20,130,30);

        create = new MyButton(true);
        create.setText("Dodaj książkę");
        create.setBounds(350, 60,130,30);

        remove = new MyButton(true);
        remove.setText("Usuń książkę");
        remove.setBounds(20,350,150,30);

        edit = new MyButton(true);
        edit.setText("Edytuj książkę");
        edit.setBounds(180,350,150,30);

        location = new MyButton(true);
        location.setText("Bez lokalizacji");
        location.setBounds(490, 20,130,30);

        unavailable = new MyButton(true);
        unavailable.setText("Niedostępne");
        unavailable.setBounds(490, 60, 130,30);

        lend = new MyButton(true);
        lend.setText("Wypożycz książkę");
        lend.setBounds(340,350,150,30);
    }

    private void actions(){

        search.addActionListener(e -> {
            List<AuthorBook> bookList = new ArrayList<>();

            if(keyWord.getText().length() == 0){
                bookList = iAuthorBook.getAllBooks(1);
            } else if(searchBy.getSelectedIndex() == 1){
                if(keyWord.getText().contains(",")) {
                    int coma = keyWord.getText().indexOf(",");
                    String firstName = keyWord.getText().substring(0, coma);
                    String lastName = keyWord.getText().substring(coma + 2);
                    int authorId = iAuthor.getAuthorId(firstName, lastName);
                    bookList = iAuthorBook.getBooksOfAuthor(authorId, 1);
                } else {
                    JOptionPane.showMessageDialog(this, "Nieprawidłowy format.");
                }
            } else if(searchBy.getSelectedIndex() == 0) {
                bookList = iAuthorBook.getBooksByTitle(keyWord.getText(), 1);
            } else {
                bookList = iAuthorBook.getBooksBySearch(keyWord.getText(), 1);
            }

            if(bookList.size() > 0) {
                createBookJList(bookList);
                add(scrollPane);
            } else {
                remove(scrollPane);
                result.setText("Nie ma takich książek.");
            }

        });

        remove.addActionListener(e ->{
            AuthorBook book = resultList.getSelectedValue();

            if(book== null) {
                JOptionPane.showMessageDialog(this, "Żadna książka nie została wybrana.");
            } else {

                if (JOptionPane.showConfirmDialog(this, "Czy na pewno usunąć książkę?", "UWAGA!",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    iAuthorBook.removeBook(book.getBook().getTitle());
                    iBook.removeBook(book.getBook().getBookId());
                    repaint();
                    revalidate();
                }
            }
        });

        location.addActionListener(e ->{

            List<AuthorBook> bookList = iAuthorBook.getNotLocatedBooks();

            if(bookList.size() > 0) {
                createBookJList(bookList);
                add(scrollPane);
            } else {
                remove(scrollPane);
                result.setText("Nie ma takich książek.");
            }
        });

        unavailable.addActionListener(e ->{
            List<AuthorBook> bookList = iAuthorBook.getAllBooks(3);

            if(bookList.size() > 0) {
                createBookJList(bookList);
                add(scrollPane);
            } else {
                remove(scrollPane);
                result.setText("Nie ma takich książek.");
            }
        });

        lend.addActionListener(e -> {

            AuthorBook book = resultList.getSelectedValue();
            List<Integer> readerCards = readerDBService.getReadersCards();

            if (book == null) {
                JOptionPane.showMessageDialog(this, "Żadna książka nie została wybrana.");
            } else if (!book.getBook().isAvailable()){
                JOptionPane.showMessageDialog(this, "Książka jest niedostępna.");
            } else {

                readerCard = JOptionPane.showInputDialog(this, "Podaj numer karty czytelnika:");

                if(check()){
                    int cardId = Integer.parseInt(readerCard);

                    if (!readerCards.contains(cardId)) {
                        JOptionPane.showMessageDialog(this, "Nie ma takiej karty.");
                    } else {
                        User user = userDBService.readUserFromDB(cardId);
                        Reader reader = readerDBService.readReaderFromDB(user.getIdUser());
                        int readerId = reader.getIdReader();

                        bookTransfer.lendBook(readerId, book.getBook().getBookId());
                        bookService.setBookAvailability(book.getBook().getBookId(), false);

                        JOptionPane.showMessageDialog(this, bookTransfer.getMessage());
                    }
                }
            }
        });
    }

    private void addComps(){

        add(searchByLabel);
        add(searchBy);
        add(keyWordLabel);
        add(keyWord);
        add(result);
        add(search);
        add(create);
        add(remove);
        add(edit);
        add(location);
        add(unavailable);
        add(lend);
    }

    public MyButton getEdit() {
        return edit;
    }

    public MyButton getCreate() {return  create;}

    private boolean check(){

        boolean isCorrect = Pattern.matches("[0-9]+", readerCard);

        if(!isCorrect)
            JOptionPane.showMessageDialog(this, "Nieprawidłowe ID.");

        return isCorrect;
    }

    int getBookIdToEdit() {

        int bookIdToEdit = 0;

        AuthorBook authorBook = resultList.getSelectedValue();
        if(authorBook != null){
            bookIdToEdit = authorBook.getBook().getBookId();
        }

        return bookIdToEdit;
    }

    int getAuthorIdToEdit(){

        int authorIdToEdit = 0;

        AuthorBook authorBook = resultList.getSelectedValue();
        if(authorBook != null) {
            authorIdToEdit = authorBook.getAuthor().getAuthorId();
        }

        return authorIdToEdit;
    }
    public JList<AuthorBook> getResultList(){return resultList;}
}
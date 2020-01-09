package gui.bookTransfer;

import book.*;
import bookTransfer.BookTransferService;
import bookTransfer.IBookTransfer;
import gui.general.MyButton;
import gui.reader.ReaderTabbedPanel;
import reader.IReaderDBService;
import reader.Reader;
import reader.ReaderDBServiceImpl;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BookReservePanel extends JPanel {

    private JLabel searchByLabel, keyWordLabel, result;
    private JComboBox<String> searchBy;
    private JTextField keyWord;
    private JList<AuthorBook> resultList;
    private JScrollPane scrollPane;
    private MyButton search, reserveBtn;
    private MyButton cancel;
    private int idUser;

    private IAuthor iAuthor = new AuthorService();
    private IBook iBook = new BookService();
    private IAuthorBook iAuthorBook = new AuthorBookService();
    private IBookTransfer iBookTransfer = new BookTransferService();
    private JLabel cardIdTxt;
    private IReaderDBService readerDBService = new ReaderDBServiceImpl();

    public BookReservePanel(ReaderTabbedPanel readerTabbedPanel) {

        this.idUser = readerTabbedPanel.getIdUser();

        setLayout(null);

        createComps();
        addComps();
        actions();
        cardIdTxt.setVisible(false);
    }

    private void createBookJList(List<AuthorBook> bookList){

        DefaultListModel<AuthorBook> listModel = new DefaultListModel<>();
        for (AuthorBook aBookList : bookList) {
            listModel.addElement(aBookList);
        }
        resultList.setModel(listModel);

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
        search.setBounds(400,20,200,30);

        reserveBtn = new MyButton(true);
        reserveBtn.setText("Zarezerwuj");
        reserveBtn.setBounds(400,60,200,30);

        cancel = new MyButton(false);
        cancel.setText("Cofnij");
        cancel.setBounds(400,350,200,30);

        cardIdTxt = new JLabel();
        cardIdTxt.setBounds(20,20,200,30);

    }

    private void actions(){

        search.addActionListener(e -> {
            List<AuthorBook> bookList = new ArrayList<>();

            if(keyWord.getText().length() == 0){
                bookList = iAuthorBook.getAllBooks(2);
            } else if(searchBy.getSelectedIndex() == 1){
                if(keyWord.getText().contains(", ")) {
                    int coma = keyWord.getText().indexOf(",");
                    String firstName = keyWord.getText().substring(0, coma);
                    String lastName = keyWord.getText().substring(coma + 2);
                    int authorId = iAuthor.getAuthorId(firstName, lastName);
                    bookList = iAuthorBook.getBooksOfAuthor(authorId, 2);
                } else {
                    JOptionPane.showMessageDialog(this, "Nieprawidlowy format.");
                }
            } else if(searchBy.getSelectedIndex() == 0) {
                bookList = iAuthorBook.getBooksByTitle(keyWord.getText(), 2);
            } else {
                bookList = iAuthorBook.getBooksBySearch(keyWord.getText(), 2);
            }

            if(bookList.size() > 0) {
                createBookJList(bookList);
                add(scrollPane);
            } else {
                remove(scrollPane);
                result.setText("Nie ma takich książek.");
            }
        });

        reserveBtn.addActionListener(e -> {

            List<AuthorBook> book = resultList.getSelectedValuesList();

            if(book.size() == 0) {
                JOptionPane.showMessageDialog(this, "Żadna książka nie została wybrana.");
            } else {
                Reader reader = readerDBService.readReaderFromDB(idUser);
                int readerId = reader.getIdReader();

                for (AuthorBook aBook : book) {
                    iBookTransfer.reserveBook(readerId, aBook.getBook().getBookId());
                    iBook.setBookAvailability(aBook.getBook().getBookId(), false);
                }
                JOptionPane.showMessageDialog(this, iBookTransfer.getMessage());

            }
        });
    }

    private void addComps(){
        add(cardIdTxt);
        add(searchByLabel);
        add(searchBy);
        add(keyWordLabel);
        add(keyWord);
        add(result);
        add(search);
        add(reserveBtn);
        add(cancel);
    }

    public MyButton getCancel() {
        return cancel;
    }

    public JLabel getCardIdTxt() {
        return cardIdTxt;
    }

    public void setCardIdTxt(JLabel cardIdTxt) {
        this.cardIdTxt = cardIdTxt;
    }
}
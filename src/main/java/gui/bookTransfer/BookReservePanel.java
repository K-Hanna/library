package gui.bookTransfer;

import book.*;
import bookTransfer.BookTransfer;
import bookTransfer.BookTransferService;
import bookTransfer.IBookTransfer;
import reader.IReaderDBService;
import reader.Reader;
import reader.ReaderDBServiceImpl;
import reservation.IReservationDBService;
import reservation.Reservation;
import reservation.ReservationDBServiceImpl;
import user.IUserDBService;
import user.User;
import user.UserDBServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookReservePanel extends JPanel {

    private JLabel searchByLabel, keyWordLabel, resultListLabel, result;
    private JComboBox searchBy;
    private JTextField keyWord;
    private JList resultList;
    private JButton search, remove, reserveBtn, returnBtn;

    private IAuthor iAuthor = new AuthorService();
    private IAuthorBook iAuthorBook = new AuthorBookService();
    private IBookTransfer iBookTransfer = new BookTransferService();
    private JLabel cardIdTxt;
    private IUserDBService userDBService = new UserDBServiceImpl();
    private IReaderDBService readerDBService = new ReaderDBServiceImpl();
    private IReservationDBService reservationDBService = new ReservationDBServiceImpl();

    public BookReservePanel() {

        setLayout(null);

        createComps();
        addComps();
        actions();
        remove.setVisible(false);
        cardIdTxt.setVisible(false);
    }

    private void createBookJList(List<AuthorBook> bookList){

        DefaultListModel listModel = new DefaultListModel();
        for (AuthorBook aBookList : bookList) {
            listModel.addElement(aBookList);
        }
        resultList.setModel(listModel);
        JScrollPane listScroller = new JScrollPane(resultList);
        listScroller.setPreferredSize(new Dimension(250, 80));
    }

    private void createComps() {

        searchByLabel = new JLabel("Wyszukaj po:");
        searchByLabel.setBounds(20,20,100,30);

        searchBy = new JComboBox(new String[]{"tytule", "autorze (imię, nazwisko)", "wydawcy", "gatunku", "języku"});
        searchBy.setBounds(150,20,200,30);

        keyWordLabel = new JLabel("Słowo kluczowe:");
        keyWordLabel.setBounds(20,60,100,30);

        keyWord = new JTextField();
        keyWord.setBounds(150,60,200,30);

        resultListLabel = new JLabel("Wyniki wyszukiwania:");
        resultListLabel.setBounds(20,100,200,30);

        resultList = new JList();
        resultList.setBounds(20,140,580,320);
        resultList.setBorder(BorderFactory.createLineBorder(Color.black));

        result = new JLabel();
        result.setBounds(20,140,580,320);
        result.setBorder(BorderFactory.createLineBorder(Color.black));
        result.setBackground(Color.white);
        result.setOpaque(true);

        search = new JButton("szukaj");
        search.setBounds(400,20,200,30);

        remove = new JButton("usuń");
        remove.setBounds(400,60,200,30);

        reserveBtn = new JButton("rezerwuj");
        reserveBtn.setBounds(400,100,200,30);

        returnBtn = new JButton("cofnij");
        returnBtn.setBounds(400,470,200,30);


        cardIdTxt = new JLabel();
        cardIdTxt.setBounds(30,30, 30,20);

    }

    private void actions(){

        search.addActionListener(e -> {
            List<AuthorBook> bookList = new ArrayList<>();

            if(keyWord.getText().length() == 0){
                bookList = iAuthorBook.getAllBooks();
            } else if(searchBy.getSelectedIndex() == 1){
                if(keyWord.getText().contains(",")) {
                    int coma = keyWord.getText().indexOf(",");
                    String firstName = keyWord.getText().substring(0, coma);
                    String lastName = keyWord.getText().substring(coma + 2);
                    int authorId = iAuthor.getAuthorId(firstName, lastName);
                    bookList = iAuthorBook.getBooksOfAuthor(authorId);
                } else {
                    JOptionPane.showMessageDialog(this, "Nieprawidlowy format");
                }
            } else if(searchBy.getSelectedIndex() == 0) {
                bookList = iAuthorBook.getBooksByTitle(keyWord.getText());
            } else {
                bookList = iAuthorBook.getBooksBySearch(keyWord.getText());
            }

            if(bookList.size() > 0) {
                createBookJList(bookList);
                add(resultList);
            } else {
                remove(resultList);
                result.setText("Nie ma takich książek.");
            }
        });

        reserveBtn.addActionListener(e -> {

            List<AuthorBook> book = resultList.getSelectedValuesList();

            User user = userDBService.readUserFromDB(Integer.parseInt(cardIdTxt.getText()));
            Reader reader = readerDBService.readReaderFromDB(user.getIdUser());
            int readerId = reader.getIdReader();

            for (AuthorBook aBook : book) {
                iBookTransfer.reserveBook(readerId, aBook.getBook().getBookId());
                JOptionPane.showMessageDialog(this, iBookTransfer.getMessage());
                repaint();
                revalidate();
            }

            if(book.size() == 0)
                JOptionPane.showMessageDialog(this, "Żadna książka nie została wybrana.");
        });
    }

    private void addComps(){
        add(cardIdTxt);
        add(searchByLabel);
        add(searchBy);
        add(keyWordLabel);
        add(keyWord);
        add(resultListLabel);
        add(result);
        add(search);
        add(remove);
        add(reserveBtn);
        add(returnBtn);
    }

    public JButton getReturnBtn() {
        return returnBtn;
    }

    public JLabel getCardIdTxt() {
        return cardIdTxt;
    }

    public void setCardIdTxt(JLabel cardIdTxt) {
        this.cardIdTxt = cardIdTxt;
    }
}
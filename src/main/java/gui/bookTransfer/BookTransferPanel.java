package gui.bookTransfer;

import book.BookService;
import book.IBook;
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

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class BookTransferPanel extends JPanel {

    private IBookTransfer bookTransfer = new BookTransferService();

    private JLabel userIdLabel, lentBooksLabel, reservedBooksLabel;
    private JTextField userId;
    private MyButton showBooks, lendBook, returnBooks, cancel;
    private JList<BookTransfer> lentBooks, reservedBooks;
    private JScrollPane scrollPane, scrollPane2;

    private IReaderDBService readerDBService = new ReaderDBServiceImpl();
    private IUserDBService userDBService = new UserDBServiceImpl();
    private IBook bookService = new BookService();

    private int readerId;

    public BookTransferPanel(){

        setLayout(null);
        createComps();
        addComps();
        actions();

    }

    private void createReservedBooks(List<BookTransfer> usersReservedBooks){

        DefaultListModel listModel = new DefaultListModel();
        for (BookTransfer aBookList : usersReservedBooks) {
            listModel.addElement(aBookList);
        }
        reservedBooks.setModel(listModel);
    }

    private void createLentBooks(List<BookTransfer> usersLentBooks) {

        DefaultListModel listModel = new DefaultListModel();
        for (BookTransfer aBookList : usersLentBooks) {
            listModel.addElement(aBookList);
        }
        lentBooks.setModel(listModel);
    }

    private void createComps(){

        userIdLabel = new JLabel("Numer karty:");
        userIdLabel.setBounds(50,20,100,30);

        userId = new JTextField();
        userId.setBounds(150,20,200, 30);

        showBooks = new MyButton(true);
        showBooks.setText("Pokaż");
        showBooks.setBounds(400,20,200,30);

        reservedBooksLabel = new JLabel();
        reservedBooksLabel.setBounds(50,60,550,100);
        reservedBooksLabel.setBorder(new TitledBorder("Zarezerwowane książki:"));
        reservedBooksLabel.setBackground(Color.white);
        reservedBooksLabel.setOpaque(true);
        reservedBooksLabel.setVerticalAlignment(1);

        reservedBooks = new JList<>();
        reservedBooks.setBounds(50,60,550,100);

        scrollPane = new JScrollPane(reservedBooks);
        scrollPane.setBounds(50,60,550,100);
        scrollPane.setBorder(new TitledBorder("Zarezerowane książki"));
        scrollPane.setBackground(Color.white);

        lendBook = new MyButton(true);
        lendBook.setText("Wypożycz");
        lendBook.setBounds(400,170,200,30);

        lentBooksLabel = new JLabel();
        lentBooksLabel.setBounds(50,220,550,100);
        lentBooksLabel.setBorder(new TitledBorder("Wypożyczone książki:"));
        lentBooksLabel.setBackground(Color.white);
        lentBooksLabel.setOpaque(true);
        lentBooksLabel.setVerticalAlignment(1);

        lentBooks = new JList<>();
        lentBooks.setBounds(50,220,550,100);

        scrollPane2 = new JScrollPane(lentBooks);
        scrollPane2.setBounds(50,220,550,100);
        scrollPane2.setBorder(new TitledBorder("Wypożyczone książki"));
        scrollPane2.setBackground(Color.white);

        returnBooks = new MyButton(true);
        returnBooks.setText("Przyjmij zwrot");
        returnBooks.setBounds(400, 330, 200,30);

        cancel = new MyButton(false);
        cancel.setText("Anuluj");
        cancel.setBounds(400,370,200,30);
    }

    private void actions(){

        showBooks.addActionListener(e -> {
            if(check()) {

                User user = userDBService.readUserFromDB(Integer.parseInt(userId.getText()));
                Reader reader = readerDBService.readReaderFromDB(user.getIdUser());
                readerId = reader.getIdReader();

                List<BookTransfer> reservedUserBooks = bookTransfer.getReservedUserBooks(readerId);
                List<BookTransfer> lentUserBooks = bookTransfer.getLentUserBooks(readerId);

                if (reservedUserBooks.size() > 0) {
                    createReservedBooks(reservedUserBooks);
                    add(scrollPane);
                } else {
                    reservedBooksLabel.setText("Brak zarezerowanych książek.");
                }

                if (lentUserBooks.size() > 0) {
                    createLentBooks(lentUserBooks);
                    add(scrollPane2);
                } else {
                    lentBooksLabel.setText("Brak wypożyczonych książek.");
                }

                if(lentUserBooks.size() >= 3){
                    lendBook.setEnabled(false);
                    lendBook.setContentAreaFilled(false);
                } else {
                    lendBook.setEnabled(true);
                    lendBook.setContentAreaFilled(true);
                }

                for (BookTransfer books : reservedUserBooks) {
                    if(books.getDuedate().before(new Date()))
                        bookTransfer.unReserveBook(books.getAuthorBook().getBook().getBookId());
                }
            }
        });

        lendBook.addActionListener(e ->{

            List<BookTransfer> book = reservedBooks.getSelectedValuesList();

            if(book.size() > 0) {
                User user = userDBService.readUserFromDB(Integer.parseInt(userId.getText()));
                Reader reader = readerDBService.readReaderFromDB(user.getIdUser());
                readerId = reader.getIdReader();

                for (BookTransfer aBook : book) {
                    bookTransfer.lendBook(readerId, aBook.getAuthorBook().getBook().getBookId());
                    bookTransfer.unReserveBook(aBook.getAuthorBook().getBook().getBookId());
                    bookService.setBookAvailability(aBook.getAuthorBook().getBook().getBookId(), false);
                }

                List<BookTransfer> reservedUserBooks = bookTransfer.getReservedUserBooks(readerId);
                List<BookTransfer> lentUserBooks = bookTransfer.getLentUserBooks(readerId);
                createReservedBooks(reservedUserBooks);
                createLentBooks(lentUserBooks);

                if(lentUserBooks.size() >= 3){
                    lendBook.setEnabled(false);
                    lendBook.setContentAreaFilled(false);
                } else {
                    lendBook.setEnabled(true);
                    lendBook.setContentAreaFilled(true);
                }

                JOptionPane.showMessageDialog(this, bookTransfer.getMessage());
            } else
                JOptionPane.showMessageDialog(this, "Żadna książka nie została wybrana.");
        });

        returnBooks.addActionListener(e ->{

            List<BookTransfer> book = lentBooks.getSelectedValuesList();

            if(book.size() == 0)
                JOptionPane.showMessageDialog(this, "Żadna książka nie została wybrana.");
            else {
                for (BookTransfer aBook : book) {
                    bookTransfer.acceptReturnBook(aBook.getAuthorBook().getBook().getBookId());
                    bookService.setBookAvailability(aBook.getAuthorBook().getBook().getBookId(), true);
                }

                List<BookTransfer> lentUserBooks = bookTransfer.getLentUserBooks(readerId);
                createLentBooks(lentUserBooks);

                if(lentUserBooks.size() >= 3){
                    lendBook.setEnabled(false);
                    lendBook.setContentAreaFilled(false);
                } else {
                    lendBook.setEnabled(true);
                    lendBook.setContentAreaFilled(true);
                }
                JOptionPane.showMessageDialog(this, bookTransfer.getMessage());
            }
        });
    }

    private void addComps(){

        add(userIdLabel);
        add(userId);
        add(showBooks);
        add(reservedBooksLabel);
        add(lendBook);
        add(lentBooksLabel);
        add(returnBooks);
        add(cancel);
    }

    private boolean check(){

        Pattern pattern = Pattern.compile("[0-9]+");
        boolean isCorrect = pattern.matcher(userId.getText()).matches();

        if(!isCorrect)
            JOptionPane.showMessageDialog(this, "Nieprawidłowe ID.");

        return isCorrect && !(userId.getText().isEmpty());
    }

    public JTextField getUserId() {
        return userId;
    }

    public MyButton getCancel() {
        return cancel;
    }
}

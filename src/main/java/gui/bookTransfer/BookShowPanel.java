package gui.bookTransfer;

import book.BookService;
import book.IBook;
import bookTransfer.BookTransfer;
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
import java.util.List;

public class BookShowPanel extends JPanel {

    private IReaderDBService readerDBService = new ReaderDBServiceImpl();
    private IBookTransfer bookTransfer = new BookTransferService();
    private IBook bookService = new BookService();

    private JLabel reservedBooksLabel, lentBooksLabel;
    private MyButton resignBook, searchBooks;
    private static JList<BookTransfer> lentBooks, reservedBooks;
    private JScrollPane scrollPane, scrollPane2;
    private Reader reader;

    private int idUser, idReader;

    public BookShowPanel(ReaderTabbedPanel readerTabbedPanel) {

        this.idUser = readerTabbedPanel.getIdUser();

        reader = readerDBService.readReaderFromDB(idUser);
        idReader = reader.getIdReader();

        setLayout(null);
        createComps();

        List<BookTransfer> reservedUserBooks = bookTransfer.getReservedUserBooks(idReader);
        List<BookTransfer> lentUserBooks = bookTransfer.getLentUserBooks(idReader);

        if(reservedUserBooks.size() > 0){
            createReservedBooks(reservedUserBooks);
            add(scrollPane);
        } else {
            reservedBooksLabel.setText("Brak zarezerwowanych książek.");
        }
        if(lentUserBooks.size() > 0) {
            createLentBooks(lentUserBooks);
            add(scrollPane2);
        } else {
            lentBooksLabel.setText("Brak wypożyczonych książek.");
        }

        addComps();
        action();
    }

    static void createReservedBooks(List<BookTransfer> bookList){

        DefaultListModel<BookTransfer> listModel = new DefaultListModel<>();
        for (BookTransfer aBookList : bookList) {
            listModel.addElement(aBookList);
        }
        reservedBooks.setModel(listModel);
    }

    private void createLentBooks(List<BookTransfer> bookList) {

        DefaultListModel<BookTransfer> listModel2 = new DefaultListModel<>();
        for (BookTransfer aBookList : bookList) {
            listModel2.addElement(aBookList);
        }
        lentBooks.setModel(listModel2);
    }

    private void addComps() {

        add(resignBook);
        add(reservedBooksLabel);
        add(lentBooksLabel);
        add(searchBooks);
    }

    private void createComps(){

        resignBook = new MyButton(true);
        resignBook.setText("Rezygnuj");
        resignBook.setBounds(400,170,200,30);

        searchBooks = new MyButton(true);
        searchBooks.setText("Szukaj książki");
        searchBooks.setBounds(400,20,200,30);

        reservedBooks = new JList();
        reservedBooks.setBounds(50,60,550,100);

        scrollPane = new JScrollPane(reservedBooks);
        scrollPane.setBounds(50,60,550,100);
        scrollPane.setBorder(new TitledBorder("Zarezerowane książki"));
        scrollPane.setBackground(Color.white);

        reservedBooksLabel = new JLabel();
        reservedBooksLabel.setBounds(50,60,550,100);
        reservedBooksLabel.setBorder(new TitledBorder("Zarezerwowane książki:"));
        reservedBooksLabel.setBackground(Color.white);
        reservedBooksLabel.setOpaque(true);
        reservedBooksLabel.setVerticalAlignment(1);

        lentBooks = new JList();
        lentBooks.setBounds(50,220,550,100);

        scrollPane2 = new JScrollPane(lentBooks);
        scrollPane2.setBounds(50,220,550,100);
        scrollPane2.setBorder(new TitledBorder("Wypożyczone książki"));
        scrollPane2.setBackground(Color.white);

        lentBooksLabel = new JLabel();
        lentBooksLabel.setBounds(50,220,550,100);
        lentBooksLabel.setBorder(new TitledBorder("Wypożyczone książki:"));
        lentBooksLabel.setBackground(Color.white);
        lentBooksLabel.setOpaque(true);
        lentBooksLabel.setVerticalAlignment(1);

    }

    private void action() {

        resignBook.addActionListener(e ->{

            List<BookTransfer> book = reservedBooks.getSelectedValuesList();

            if(book.size() > 0 ) {
                for (BookTransfer aBook : book) {
                    bookTransfer.unReserveBook(aBook.getAuthorBook().getBook().getBookId());
                    bookService.setBookAvailability(aBook.getAuthorBook().getBook().getBookId(), true);
                }

                List<BookTransfer> reservedUserBooks = bookTransfer.getReservedUserBooks(idReader);
                if(reservedUserBooks.size() > 0){
                    createReservedBooks(reservedUserBooks);
                } else {
                    reservedBooksLabel.setText("Brak zarezerowanych książek.");
                }

                JOptionPane.showMessageDialog(this, "Zrezygnowano z książek.");
            } else {
                JOptionPane.showMessageDialog(this, "Żadna książka nie została wybrana.");
            }
        });
    }

    public MyButton getSearchBooks(){return searchBooks;}
}

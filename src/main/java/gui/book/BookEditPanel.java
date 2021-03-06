package gui.book;

import book.*;
import gui.general.MyButton;

import javax.swing.*;
import java.awt.*;

public class BookEditPanel extends JPanel {

    private JLabel titleLabel, publisherLabel, genreLabel, languageLabel;
    private JTextField publisher, genre, language;
    private JTextArea title;
    private JLabel firstNameLabel, lastNameLabel;
    private JTextField firstName, lastName;
    private JLabel alleyLabel, bookstandLabel, shelfLabel;
    private JComboBox alley, bookstand, shelf;
    private MyButton confirm, cancel;
    private int fieldLength = 200;

    private IBook bookService = new BookService();
    private IAuthor authorService = new AuthorService();
    private IAuthorBook authorBookService = new AuthorBookService();
    private IBookshelf bookshelfService = new BookshelfService();
    private int bookIdToEdit, authorIdToEdit;
    private Author author;
    private Book book;

    public BookEditPanel(BookGetPanel bookGetPanel) {

        this.bookIdToEdit = bookGetPanel.getBookIdToEdit();
        this.authorIdToEdit = bookGetPanel.getAuthorIdToEdit();
        book = bookService.getBook(bookIdToEdit);
        if(book == null)
            book = bookService.getNullBook(bookIdToEdit);
        author = authorService.getAuthor(authorIdToEdit);

        setLayout(null);

        createComps();
        addComp();
        action();
    }

    private void createComps() {

        titleLabel = new JLabel("Tytuł:");
        titleLabel.setBounds(50, 20, 100, 30);

        title = new JTextArea();
        title.setBounds(180, 20, fieldLength, 55);
        title.setBorder(BorderFactory.createLineBorder(Color.black));
        title.setLineWrap(true);
        title.setText(book.getTitle());

        firstNameLabel = new JLabel("Imię autora:");
        firstNameLabel.setBounds(50, 85, 100, 30);

        firstName = new JTextField();
        firstName.setBounds(180, 85, fieldLength, 30);
        firstName.setText(author.getFirstName());

        lastNameLabel = new JLabel("Nazwisko autora: ");
        lastNameLabel.setBounds(50, 125, 120, 30);

        lastName = new JTextField();
        lastName.setBounds(180, 125, fieldLength, 30);
        lastName.setText(author.getLastName());

        publisherLabel = new JLabel("Wydawca:");
        publisherLabel.setBounds(50, 165, 100, 30);

        publisher = new JTextField();
        publisher.setBounds(180, 165, fieldLength, 30);
        publisher.setText(book.getPublisher());

        genreLabel = new JLabel("Gatunek:");
        genreLabel.setBounds(50, 205, 100, 30);

        genre = new JTextField();
        genre.setBounds(180, 205, fieldLength, 30);
        genre.setText(book.getGenre());

        languageLabel = new JLabel("Język:");
        languageLabel.setBounds(50, 245, 100, 30);

        language = new JTextField();
        language.setBounds(180, 245, fieldLength, 30);
        language.setText(book.getLanguage());

        alleyLabel = new JLabel("Alejka:");
        alleyLabel.setBounds(50, 285, 55, 30);

        String[] alleys = bookshelfService.getAlleys();
        alley = new JComboBox(alleys);
        alley.setBounds(105, 285, 55, 30);
        String alleyBook = book.getBookshelf().getAlley();
        alley.setSelectedItem(alleyBook);

        bookstandLabel = new JLabel("regał: ");
        bookstandLabel.setBounds(165,285,55,30);

        String[] bookstands = bookshelfService.getBookstands();
        bookstand = new JComboBox(bookstands);
        bookstand.setBounds(215,285,55,30);
        String bookstandBook = book.getBookshelf().getBookstand();
        bookstand.setSelectedItem(bookstandBook);

        shelfLabel = new JLabel("półka:");
        shelfLabel.setBounds(275,285,55,30);

        String[] shelves = bookshelfService.getShelves();
        shelf = new JComboBox(shelves);
        shelf.setBounds(325,285,55,30);
        String shelfBook = String.valueOf(book.getBookshelf().getShelf());
        shelf.setSelectedItem(shelfBook);

        confirm = new MyButton(true);
        confirm.setText("Zatwierdź");
        confirm.setBounds(400, 20, 200, 30);

        cancel = new MyButton(false);
        cancel.setText("Anuluj");
        cancel.setBounds(400, 60, 200, 30);
    }

    private void addComp() {
        add(titleLabel);
        add(title);
        add(publisherLabel);
        add(publisher);
        add(genreLabel);
        add(genre);
        add(languageLabel);
        add(language);
        add(firstNameLabel);
        add(firstName);
        add(lastNameLabel);
        add(lastName);
        add(alleyLabel);
        add(alley);
        add(bookstandLabel);
        add(bookstand);
        add(shelfLabel);
        add(shelf);
        add(confirm);
        add(cancel);
    }

    private void action() {

        confirm.addActionListener(e -> {
            if(check()) {
                if(book == null || author == null)
                    JOptionPane.showMessageDialog(this,"Proszę zaznaczyć książkę");
                if(!(firstName.getText().equals(author.getFirstName())) || !(lastName.getText().equals(author.getLastName()))) {
                    authorService.addAuthor(firstName.getText(), lastName.getText());
                    authorBookService.editAuthorBook(bookIdToEdit, firstName.getText(), lastName.getText());
                }
                if(!(alley.getSelectedItem().equals(book.getBookshelf().getAlley())) || !(bookstand.getSelectedItem().equals(book.getBookshelf().getBookstand())) || !(shelf.getSelectedItem().equals(String.valueOf(book.getBookshelf().getShelf()))))
                    bookService.editBook(bookIdToEdit, alley.getSelectedItem().toString(), bookstand.getSelectedItem().toString(), Integer.parseInt(shelf.getSelectedItem().toString()));
                if(!(title.getText().equals(book.getTitle())) || !(publisher.getText().equals(book.getPublisher())) || !(genre.getText().equals(book.getGenre())) || !(language.getText().equals(book.getLanguage())))
                    bookService.editBook(bookIdToEdit, title.getText(), publisher.getText(), genre.getText(), language.getText());

                JOptionPane.showMessageDialog(this, "Książka została zaktualizowana");
            } else {
                JOptionPane.showMessageDialog(this, "Uzupełnij dane.");
            }
        });
    }

    private boolean check(){

        boolean titleCheck = title.getText().length() > 0 && title.getText().length() < 60;
        boolean firstNameCheck = firstName.getText().length() > 0 && firstName.getText().length() < 20;
        boolean lastNameCheck = lastName.getText().length() > 0 && lastName.getText().length() < 20;
        boolean publisherCheck = publisher.getText().length() > 0 && publisher.getText().length() < 20;
        boolean genreCheck = genre.getText().length() > 0 && genre.getText().length() < 20;
        boolean languageCheck = language.getText().length() > 0 && language.getText().length() < 20;

        return titleCheck && firstNameCheck && lastNameCheck && publisherCheck &&
                genreCheck && languageCheck;
    }

    public MyButton getCancel() {return  cancel;}
}
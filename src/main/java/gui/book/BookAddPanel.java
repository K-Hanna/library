package gui.book;

import book.*;
import gui.general.MyButton;

import javax.swing.*;
import java.awt.*;

public class BookAddPanel extends JPanel {

    private JLabel titleLabel, publisherLabel, genreLabel, languageLabel;
    private JTextField publisher, genre, language;
    private JTextArea title;
    private JLabel firstNameLabel, lastNameLabel, moreAuthorsLabel;
    private JTextField firstName, lastName;
    private JTextField[] names;
    private JLabel alleyLabel, bookstandLabel, shelfLabel;
    private JComboBox alley, bookstand, shelf;
    private MyButton confirm, cancel;
    private JRadioButton oneAuthor, moreAuthors;
    private String alertMessage;

    private IAuthor authorService = new AuthorService();
    private IBook bookService = new BookService();
    private IAuthorBook authorBookService = new AuthorBookService();
    private IBookshelf bookshelfService = new BookshelfService();


    public BookAddPanel() {

        setLayout(null);

        createComps();
        addComp();
        action();
        setMoreSeen(false);

    }

    private void createComps() {

        titleLabel = new JLabel("Tytuł:");
        titleLabel.setBounds(50, 20, 100, 30);

        title = new JTextArea();
        title.setBounds(180, 20, 200, 55);
        title.setBorder(BorderFactory.createLineBorder(Color.black));
        title.setLineWrap(true);

        firstNameLabel = new JLabel("Imię autora:");
        firstNameLabel.setBounds(50, 85, 100, 30);

        firstName = new JTextField();
        firstName.setBounds(180, 85, 200, 30);

        lastNameLabel = new JLabel("Nazwisko autora: ");
        lastNameLabel.setBounds(50, 125, 120, 30);

        lastName = new JTextField();
        lastName.setBounds(180, 125, 200, 30);

        publisherLabel = new JLabel("Wydawca:");
        publisherLabel.setBounds(50, 165, 100, 30);

        publisher = new JTextField();
        publisher.setBounds(180, 165, 200, 30);

        genreLabel = new JLabel("Gatunek:");
        genreLabel.setBounds(50, 205, 100, 30);

        genre = new JTextField();
        genre.setBounds(180, 205, 200, 30);

        languageLabel = new JLabel("Język:");
        languageLabel.setBounds(50, 245, 100, 30);

        language = new JTextField();
        language.setBounds(180, 245, 200, 30);

        alleyLabel = new JLabel("Alejka:");
        alleyLabel.setBounds(50, 285, 55,30);

        String[] alleys = bookshelfService.getAlleys();
        alley = new JComboBox(alleys);
        alley.setBounds(105, 285, 55, 30);

        bookstandLabel = new JLabel("regał: ");
        bookstandLabel.setBounds(165,285,55,30);

        String[] bookstands = bookshelfService.getBookstands();
        bookstand = new JComboBox(bookstands);
        bookstand.setBounds(215,285,55,30);

        shelfLabel = new JLabel("półka:");
        shelfLabel.setBounds(275,285,55,30);

        String[] shelves = bookshelfService.getShelves();
        shelf = new JComboBox(shelves);
        shelf.setBounds(325,285,55,30);

        moreAuthorsLabel = new JLabel("Autorzy:");
        moreAuthorsLabel.setBounds(50,85,200,30);
        moreAuthorsLabel.setVisible(false);

        names = new JTextField[6];
        for (int i = 0; i < names.length; i++) {
            names[i] = new JTextField();
            if(i%2 == 0)
                names[i].setBounds(180,85 + (25 * (i / 2)), 200,25);
            else
                names[i].setBounds(380,85 + (25 * (i / 2)), 200,25);
        }

        oneAuthor = new JRadioButton("1 autor");
        oneAuthor.setBounds(400,20,200,30);
        oneAuthor.setSelected(true);

        moreAuthors = new JRadioButton("więcej autorów");
        moreAuthors.setBounds(400,50,200,30);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(oneAuthor);
        buttonGroup.add(moreAuthors);

        confirm = new MyButton(true);
        confirm.setText("Dodaj");
        confirm.setBounds(400, 245, 200, 30);

        cancel = new MyButton(false);
        cancel.setText("Anuluj");
        cancel.setBounds(400, 285, 200, 30);
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
        add(oneAuthor);
        add(moreAuthors);
        add(moreAuthorsLabel);
        add(confirm);
        add(cancel);
        for (JTextField name : names) {
            add(name);
        }
    }

    private void action() {

        confirm.addActionListener(e1 -> {
            if(oneAuthor.isSelected()) {
                if (check()) {

                    authorService.addAuthor(firstName.getText(), lastName.getText());
                    bookService.addBook(title.getText(), genre.getText(), publisher.getText(), language.getText(),
                            alley.getSelectedItem().toString(), bookstand.getSelectedItem().toString(), Integer.parseInt(shelf.getSelectedItem().toString()));
                    authorBookService.addAuthorBook(firstName.getText(), lastName.getText(), title.getText());
                    JOptionPane.showMessageDialog(this, "Książka została dodana do bazy");

                } else {
                    JOptionPane.showMessageDialog(this, alertMessage);
                }
            } else {
                firstName.setText("none");
                lastName.setText("none");
                if(check()) {
                    bookService.addBook(title.getText(), genre.getText(), publisher.getText(), language.getText(),
                            alley.getSelectedItem().toString(), bookstand.getSelectedItem().toString(), Integer.parseInt(shelf.getSelectedItem().toString()));
                    for (int i = 0; i < names.length; i+= 2){
                        if(names[i].getText().length() > 0) {
                            authorService.addAuthor(names[i].getText(), names[i + 1].getText());
                            authorBookService.addAuthorBook(names[i].getText(), names[i + 1].getText(), title.getText());
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Książka została dodana do bazy");

                } else {
                    JOptionPane.showMessageDialog(this, alertMessage);
                }
            }
        });

        oneAuthor.addActionListener(e ->{
            setMoreSeen(false);
            setOneSeen(true);
        });

        moreAuthors.addActionListener(e ->{
            setMoreSeen(true);
            setOneSeen(false);
        });
    }

    private boolean check(){

        alertMessage = "Uzupełnij dane: ";

        boolean titleCheck = title.getText().length() > 0 && title.getText().length() < 50;
        if(!titleCheck)
            alertMessage = alertMessage + "\n- brak tytułu";
        boolean firstNameCheck = firstName.getText().length() > 0 && firstName.getText().length() < 20;
        if(!firstNameCheck)
            alertMessage = alertMessage + "\n- brak imienia";
        boolean lastNameCheck = lastName.getText().length() > 0 && lastName.getText().length() < 20;
        if(!lastNameCheck)
            alertMessage = alertMessage + "\n- brak nazwiska";
        boolean publisherCheck = publisher.getText().length() > 0 && publisher.getText().length() < 20;
        if(!publisherCheck)
            alertMessage = alertMessage +  "\n- brak wydawcy";
        boolean genreCheck = genre.getText().length() > 0 && genre.getText().length() < 20;
        if(!genreCheck)
            alertMessage = alertMessage + "\n- brak gatunku";
        boolean languageCheck = language.getText().length() > 0 && language.getText().length() < 20;
        if (!languageCheck)
            alertMessage = alertMessage + "\n- brak języka";
        alertMessage = alertMessage + ".";

        return titleCheck && firstNameCheck && lastNameCheck && publisherCheck &&
                genreCheck && languageCheck;
    }

    private void setMoreSeen(boolean seen){
        moreAuthorsLabel.setVisible(seen);
        for (JTextField name : names) {
            name.setVisible(seen);
        }
    }

    private void setOneSeen(boolean seen){
        firstNameLabel.setVisible(seen);
        firstName.setVisible(seen);
        lastNameLabel.setVisible(seen);
        lastName.setVisible(seen);
    }

    public MyButton getCancel() {
        return cancel;
    }
}

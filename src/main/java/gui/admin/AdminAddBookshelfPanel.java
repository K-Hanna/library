package gui.admin;

import book.BookshelfService;
import book.IBookshelf;
import gui.general.MyButton;

import javax.swing.*;
import java.util.regex.Pattern;

public class AdminAddBookshelfPanel extends JPanel {

    private JLabel alleyLbl, bookstandLbl, shelfLbl;
    private JTextField alley, bookstand, shelf;
    private MyButton add, remove;

    private IBookshelf bookshelf = new BookshelfService();

    public AdminAddBookshelfPanel(){

        setLayout(null);
        createComps();
        createButtons();
        actions();
        addComps();
    }

    private void createComps(){
        alleyLbl = new JLabel("Alejka: ", SwingConstants.RIGHT);
        alleyLbl.setBounds(170,150,50,30);

        alley = new JTextField();
        alley.setBounds(220,150,50,30);
        alley.setToolTipText("1 wielka litera alfabetu łacińskiego.");

        bookstandLbl = new JLabel("Regał: ",SwingConstants.RIGHT);
        bookstandLbl.setBounds(270,150,50,30);

        bookstand = new JTextField();
        bookstand.setBounds(320,150,50,30);
        bookstand.setToolTipText("1 mała litera alfabetu łacińskiego.");

        shelfLbl = new JLabel("Półka: ", SwingConstants.RIGHT);
        shelfLbl.setBounds(370,150,50,30);

        shelf = new JTextField();
        shelf.setBounds(420,150,50,30);
        shelf.setToolTipText("1 cyfra.");
    }

    private void createButtons(){
        add = new MyButton(true);
        add.setBounds(170,200,150,30);
        add.setText("Dodaj");

        remove = new MyButton(true);
        remove.setBounds(320,200,150,30);
        remove.setText("Usuń");
    }

    private void addComps(){
        add(alleyLbl);
        add(alley);
        add(bookstandLbl);
        add(bookstand);
        add(shelfLbl);
        add(shelf);
        add(add);
        add(remove);
    }

    private void actions(){
        add.addActionListener(e -> {
            if(check()) {
                bookshelf.addBookshelf(alley.getText(), bookstand.getText(), Integer.parseInt(shelf.getText()));
                JOptionPane.showMessageDialog(this, "Dodano lokalizację.");
            } else
                JOptionPane.showMessageDialog(this, "Nieprawidłowy format.");
        });

        remove.addActionListener(e -> {
            if(check()) {
                bookshelf.removeBookshelf(alley.getText(), bookstand.getText(), Integer.parseInt(shelf.getText()));
                JOptionPane.showMessageDialog(this, "Usunięto lokalizację.");
            } else
                JOptionPane.showMessageDialog(this, "Nieprawidłowy format.");
        });
    }

    private boolean check(){
        boolean alleyNotEmpty = !alley.getText().isEmpty();
        boolean bookstandNotEmpty = !bookstand.getText().isEmpty();
        boolean shelfNotEmpty = !shelf.getText().isEmpty();

        boolean alleyCorrect = Pattern.matches("[A-Z]", alley.getText());
        boolean bookstandCorrect = Pattern.matches("[a-z]", bookstand.getText());
        boolean shelfCorrect = Pattern.matches("[0-9]", shelf.getText());

        return alleyNotEmpty && alleyCorrect && bookstandNotEmpty && bookstandCorrect && shelfNotEmpty && shelfCorrect;
    }

}

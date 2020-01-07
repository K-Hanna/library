package gui.book;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

import book.*;
import gui.general.MyButton;
import reader.IReaderDBService;
import reader.ReaderDBServiceImpl;

public class AuthorGetPanel extends JPanel {

    private JLabel keyWordLabel, result, newFirstNameLabel, newLastNameLabel;
    private JTextField keyWord, newFirstName, newLastName;
    private JList resultList;
    private MyButton search, remove, edit, change;
    private JScrollPane scrollPane;

    private IAuthor authorService = new AuthorService();
    private IAuthorBook authorBookService = new AuthorBookService();

    public AuthorGetPanel() {

        setLayout(null);

        createComps();
        addComps();
        actions();
    }

    private void createAuthorsJList(List<Author> authorList){

        DefaultListModel listModel = new DefaultListModel();
        for (Author author : authorList) {
            listModel.addElement(author);
        }

        resultList.setModel(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }

    private void createComps() {

        keyWordLabel = new JLabel("Słowo kluczowe:");
        keyWordLabel.setBounds(50,20,100,30);

        keyWord = new JTextField();
        keyWord.setBounds(150,20,200,30);

        resultList = new JList();
        resultList.setBounds(50,60,300,290);

        scrollPane = new JScrollPane(resultList);
        scrollPane.setBounds(50,60,300,290);
        scrollPane.setBorder(new TitledBorder("Wyniki wyszukiwania:"));
        scrollPane.setBackground(Color.white);

        result = new JLabel();
        result.setBounds(50,60,300,290);
        result.setBorder(new TitledBorder("Wyniki wyszukiwania:"));
        result.setBackground(Color.white);
        result.setOpaque(true);
        result.setVerticalAlignment(1);

        search = new MyButton(true);
        search.setText("Szukaj");
        search.setBounds(400,20,200,30);

        remove = new MyButton(true);
        remove.setText("Usuń");
        remove.setBounds(400,60,200,30);

        edit = new MyButton(true);
        edit.setText("Edytuj");
        edit.setBounds(400,100,200,30);

        newFirstNameLabel = new JLabel("Nowe imię:");
        newFirstNameLabel.setBounds(400,160,200,30);

        newFirstName = new JTextField();
        newFirstName.setBounds(400,200,200,30);
        newFirstName.setEditable(false);

        newLastNameLabel = new JLabel("Nowe nazwisko:");
        newLastNameLabel.setBounds(400,240,200,30);

        newLastName = new JTextField();
        newLastName.setBounds(400,280,200,30);
        newLastName.setEditable(false);

        change = new MyButton(true);
        change.setText("Zatwierdź");
        change.setBounds(400,320,200,30);
        change.setEnabled(false);
        change.setContentAreaFilled(false);

    }

    private void actions(){

        search.addActionListener(e -> {
            List<Author> authorList;

            if(keyWord.getText().length() == 0)
                authorList = authorService.getAuthors();
            else {
                authorList = authorService.getAuthors(keyWord.getText());
            }

            if(authorList.size() > 0) {
                createAuthorsJList(authorList);
                add(scrollPane);
            } else {
                remove(scrollPane);
                result.setText("Nie ma takich autorów.");
            }

        });

        remove.addActionListener(e ->{
            Author author = (Author) resultList.getSelectedValue();

            if(author == null) {
                JOptionPane.showMessageDialog(this, "Żaden autor nie został wybrany.");
            } else {

                if (JOptionPane.showConfirmDialog(this, "Czy na pewno usunąć autora?", "UWAGA!",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    authorBookService.removeBooksOfAuthor(author.getFirstName(), author.getLastName());
                    authorService.removeAuthor(author.getAuthorId());
                    repaint();
                    revalidate();
                }
            }
        });

        edit.addActionListener(e ->{

            Author author = (Author) resultList.getSelectedValue();
            if(author == null){
                JOptionPane.showMessageDialog(null, "Żaden autor nie został wybrany.");
            } else {
                newFirstName.setEditable(true);
                newFirstName.setText(author.getFirstName());
                newLastName.setEditable(true);
                newLastName.setText(author.getLastName());
                change.setEnabled(true);
                change.setContentAreaFilled(true);

                change.addActionListener(e1 ->{
                    authorService.editAuthor(author.getAuthorId(), newFirstName.getText(), newLastName.getText());
                    result.setText(authorService.getMessage());
                });
            }

        });
    }

    private void addComps(){

        add(keyWordLabel);
        add(keyWord);
        add(result);
        add(search);
        add(remove);
        add(edit);
        add(newFirstNameLabel);
        add(newFirstName);
        add(newLastNameLabel);
        add(newLastName);
        add(change);
    }

}
package gui.librarian;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

import book.*;
import gui.general.MyButton;
import librarian.ILibrarianDBService;
import librarian.Librarian;
import librarian.LibrarianDBServiceImpl;
import reader.IReaderDBService;
import reader.ReaderDBServiceImpl;
import user.IUserDBService;
import user.User;
import user.UserDBServiceImpl;

public class LibrarianGetPanel extends JPanel {

    private static JList<Librarian> resultList;
    private MyButton create, remove, show;
    private JScrollPane scrollPane;

    private ILibrarianDBService librarianDBService = new LibrarianDBServiceImpl();
    private IUserDBService userDBService = new UserDBServiceImpl();

    public LibrarianGetPanel() {

        setLayout(null);
        createComps();

        List<Librarian> librarians = librarianDBService.getAllLibrariansFromDB();
        createLibrariansJList(librarians);
        add(scrollPane);

        addComps();
        actions();
    }

    static void createLibrariansJList(List<Librarian> librarians){

        DefaultListModel listModel = new DefaultListModel();
        for (Librarian librarian : librarians) {
            listModel.addElement(librarian);
        }

        resultList.setModel(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }

    private void createComps() {


        resultList = new JList();
        resultList.setBounds(50,20,300,290);

        scrollPane = new JScrollPane(resultList);
        scrollPane.setBounds(50,20,300,290);
        scrollPane.setBorder(new TitledBorder("Wyniki wyszukiwania:"));
        scrollPane.setBackground(Color.white);

        create = new MyButton(true);
        create.setText("Dodaj bibliotekarza");
        create.setBounds(400,20,200,30);

        show = new MyButton(true);
        show.setText("Pokaż bibliotekarza");
        show.setBounds(400,60,200,30);

        remove = new MyButton(true);
        remove.setText("Usuń bibliotekarza");
        remove.setBounds(400,100,200,30);

    }

    private void actions(){

        remove.addActionListener(e ->{
            Librarian librarian = resultList.getSelectedValue();

            if(librarian == null) {
                JOptionPane.showMessageDialog(this, "Żaden bibliotekarz nie został wybrany.");
            } else {

                if (JOptionPane.showConfirmDialog(this, "Czy na pewno usunąć bibliotekarza?", "UWAGA!",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    librarianDBService.deleteLibrarianFromDB(librarian.getUserId());
                    userDBService.deleteUserFromDB(librarian.getCardNumber());

                    List<Librarian> librarians = librarianDBService.getAllLibrariansFromDB();
                    createLibrariansJList(librarians);
                }
            }
        });
    }

    private void addComps(){

        add(create);
        add(remove);
        add(show);
    }

    public int getLibrarianCard(){

        int librarianCard = 0;

        User user =resultList.getSelectedValue();
        if(user != null) {
            librarianCard = user.getCardNumber();
        }

        return librarianCard;
    }

    public MyButton getShow(){
        return show;
    }
    public MyButton getCreate(){
        return create;
    }

    public JList getResultList(){
        return resultList;
    }
}
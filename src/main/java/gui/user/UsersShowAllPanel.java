package gui.user;

import book.*;
import card.CardDBServiceImpl;
import card.ICardDBService;
import city.CityDBServiceImpl;
import city.ICityDBService;
import gui.general.MyButton;
import reader.IReaderDBService;
import reader.ReaderDBServiceImpl;
import user.IUserDBService;
import user.User;
import user.UserDBServiceImpl;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UsersShowAllPanel extends JPanel {

    private JList<User> userJList;

    private JLabel keyWordLabel, result;
    private JTextField keyWord;
    private JList resultList;
    private MyButton search, remove, showBooks;
    private JScrollPane scrollPane;

    private IUserDBService userDBService = new UserDBServiceImpl();
    private ICardDBService cardDBService = new CardDBServiceImpl();
    private ICityDBService cityDBService = new CityDBServiceImpl();
    private IReaderDBService readerDBService = new ReaderDBServiceImpl();

    public UsersShowAllPanel(){

        setLayout(null);

        createComps();
        addComps();
        actions();
    }

    private void createUserList(List<User> userList){

        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < userList.size(); i++)
        {
            listModel.addElement(userList.get(i));
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

        showBooks = new MyButton(true);
        showBooks.setText("Pokaż książki czytelnika");
        showBooks.setBounds(400,100,200,30);

    }

    private void actions(){

        search.addActionListener(e -> {
            List<User> userList;

            if(keyWord.getText().length() == 0) {
                userList = userDBService.getAllUsersFromDB();
            } else if(check()){
                userList = userDBService.readUsersFromDB(Integer.parseInt(keyWord.getText()));
            } else if(keyWord.getText().contains(" ")) {
                int coma = keyWord.getText().indexOf(" ");
                String firstName = keyWord.getText().substring(0, coma);
                String lastName = keyWord.getText().substring(coma + 1);
                userList = userDBService.readUsersFromDB(firstName, lastName);
            } else {
                userList = userDBService.readUsersFromDB(keyWord.getText());
            }

            if(userList.size() > 0) {
                createUserList(userList);
                add(scrollPane);
            } else {
                remove(scrollPane);
                result.setText("Nie ma takich czytelników.");
            }
        });

        remove.addActionListener(e ->{
            User user = (User) resultList.getSelectedValue();

            if(user == null) {
                JOptionPane.showMessageDialog(this, "Żaden czytelnik nie został wybrany.");
            } else {

                if (JOptionPane.showConfirmDialog(this, "Czy na pewno usunąć czytelnika?", "UWAGA!",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    readerDBService.deleteReaderFromDB(user.getIdUser());
                    userDBService.deleteUserFromDB(user.getCardNumber());
                    repaint();
                    revalidate();
                }
            }
        });
    }

    private void addComps(){

        add(keyWordLabel);
        add(keyWord);
        add(result);
        add(search);
        add(remove);
        add(showBooks);
    }

    public MyButton getShowBooks() {
        return showBooks;
    }

    public int getUsersBooks(){

        int usersID = 0;

        User user = (User) resultList.getSelectedValue();
        if(user != null) {
            usersID = user.getCardNumber();
        }

        return usersID;
    }

    private boolean check(){

        return Pattern.matches("[0-9]+", keyWord.getText());
    }

    public JList<User> getResultList(){return resultList;}
}

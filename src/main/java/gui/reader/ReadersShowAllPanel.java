package gui.reader;

import gui.general.MyButton;
import reader.IReaderDBService;
import reader.ReaderDBServiceImpl;
import reader.Reader;
import user.IUserDBService;
import user.User;
import user.UserDBServiceImpl;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class ReadersShowAllPanel extends JPanel {

    private JLabel keyWordLabel, result;
    private JTextField keyWord;
    private JList resultList;
    private MyButton search, remove, showBooks, showReader;
    private JScrollPane scrollPane;

    private IReaderDBService readerDBService = new ReaderDBServiceImpl();
    private IUserDBService userDBService = new UserDBServiceImpl();

    public ReadersShowAllPanel(){

        setLayout(null);

        createComps();
        addComps();
        actions();
    }

    private void createReaderList(List<User> readerList){

        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < readerList.size(); i++)
        {
            listModel.addElement(readerList.get(i));
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

        showReader = new MyButton(true);
        showReader.setText("Pokaż dane czytelnika");
        showReader.setBounds(400,100,200,30);

        showBooks = new MyButton(true);
        showBooks.setText("Pokaż książki czytelnika");
        showBooks.setBounds(400,140,200,30);

    }

    private void actions(){

        search.addActionListener(e -> {
            List<User> readerList;

            if(keyWord.getText().length() == 0) {
                readerList = readerDBService.readReadersFromDB();
            } else if(check()){
                readerList = readerDBService.readReadersFromDB(Integer.parseInt(keyWord.getText()));
            } else if(keyWord.getText().contains(" ")) {
                int coma = keyWord.getText().indexOf(" ");
                String firstName = keyWord.getText().substring(0, coma);
                String lastName = keyWord.getText().substring(coma + 1);
                readerList = readerDBService.readReadersFromDB(firstName, lastName);
            } else {
                readerList = readerDBService.readReadersFromDB(keyWord.getText());
            }

            if(readerList.size() > 0) {
                createReaderList(readerList);
                add(scrollPane);
            } else {
                remove(scrollPane);
                result.setText("Nie ma takich czytelników.");
            }
        });

        remove.addActionListener(e ->{
            User user = (User) resultList.getSelectedValue();
            int userCard = user.getCardNumber();

            if(user == null) {
                JOptionPane.showMessageDialog(this, "Żaden czytelnik nie został wybrany.");
            } else {

                if (JOptionPane.showConfirmDialog(this, "Czy na pewno usunąć czytelnika?", "UWAGA!",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    readerDBService.deleteReaderFromDB(user.getIdUser());
                    userDBService.deleteUserFromDB(userCard);

                    List<User> readers = readerDBService.readReadersFromDB();
                    createReaderList(readers);

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
        add(showReader);
        add(showBooks);
    }

    public MyButton getShowBooks() {
        return showBooks;
    }

    public MyButton getShowReader(){
        return showReader;
    }

    public int getReadersCard(){

        int readersCard = 0;

        User user = (User) resultList.getSelectedValue();
        if(user != null) {
            readersCard = user.getCardNumber();
        }

        return readersCard;
    }

    private boolean check(){

        return Pattern.matches("[0-9]+", keyWord.getText());
    }

    public JList<Reader> getResultList(){return resultList;}
}

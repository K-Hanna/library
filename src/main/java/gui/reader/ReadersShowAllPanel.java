package gui.reader;

import book.*;
import card.CardDBServiceImpl;
import card.ICardDBService;
import city.CityDBServiceImpl;
import city.ICityDBService;
import gui.general.MyButton;
import reader.IReaderDBService;
import reader.Reader;
import reader.ReaderDBServiceImpl;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class ReadersShowAllPanel extends JPanel {

    private JList<Reader> ReaderJList;

    private JLabel keyWordLabel, result;
    private JTextField keyWord;
    private JList resultList;
    private MyButton search, remove, edit;
    private JScrollPane listScroller;

    private IReaderDBService readerDBService = new ReaderDBServiceImpl();
    private ICardDBService cardDBService = new CardDBServiceImpl();
    private ICityDBService cityDBService = new CityDBServiceImpl();

    public ReadersShowAllPanel(){

        setLayout(null);

        createComps();
        addComps();
        actions();
    }

    private void createReaderList(List<Reader> ReaderList){

        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < ReaderList.size(); i++)
        {
            listModel.addElement(ReaderList.get(i));
        }

        resultList.setModel(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listScroller = new JScrollPane(resultList);
        listScroller.setPreferredSize(new Dimension(250, 80));

    }

    private void createComps() {

        keyWordLabel = new JLabel("Słowo kluczowe:");
        keyWordLabel.setBounds(50,20,100,30);

        keyWord = new JTextField();
        keyWord.setBounds(150,20,200,30);

        resultList = new JList();
        resultList.setBounds(50,60,300,290);
        resultList.setBorder(new TitledBorder("Wyniki wyszukiwania:"));

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

    }

    private void actions(){

        search.addActionListener(e -> {
            List<Reader> ReaderList;

//            if(keyWord.getText().length() == 0)
            ReaderList = readerDBService.getAllReadersFromDB();
//            else {
//                authorList = authorService.getAuthors(keyWord.getText());
//            }

            if(ReaderList.size() > 0) {
                createReaderList(ReaderList);
                add(resultList);
            } else {
                result.setText("Nie ma takich czytelników.");
            }

        });

        remove.addActionListener(e ->{
            Reader Reader = (Reader) resultList.getSelectedValue();

            if(Reader == null) {
                JOptionPane.showMessageDialog(this, "Żaden czytelnik nie został wybrany.");
            } else {

                if (JOptionPane.showConfirmDialog(this, "Czy na pewno usunąć czytelnika?", "UWAGA!",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    readerDBService.deleteReaderFromDB(Reader.getIdReader());
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
        add(edit);
    }

}

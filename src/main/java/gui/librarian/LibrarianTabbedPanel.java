package gui.librarian;

import gui.book.*;
import gui.bookTransfer.BookTransferPanel;
import gui.event.*;
import gui.general.BackgroundLabel;
import gui.general.MyButton;
import gui.reader.ReaderShowDetailPanel;
import gui.reader.ReadersShowAllPanel;

import javax.swing.*;

public class LibrarianTabbedPanel extends JPanel {

    private BookGetPanel bookGetPanel = new BookGetPanel();
    private AuthorGetPanel authorGetPanel = new AuthorGetPanel();
    private EventGetPanel eventGetPanel = new EventGetPanel();
    private ReadersShowAllPanel readersShowAllPanel = new ReadersShowAllPanel();
    private JTabbedPane tabbedPane = new JTabbedPane();

    private MyButton logout;
    private JLabel librarianLabel;
    private String start = "<html><body><table width='137' height='50'>";
    private String finish = "</table></body></html>";

    public LibrarianTabbedPanel(){

        setLayout(null);

        createLogout();
        createLibrarian();
        createTabbedPane();
        action();
        createBackground();
    }

    private void createTabbedPane(){

        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(20,20,640,500);
        setBorder(BorderFactory.createDashedBorder(null));
        setOpaque(false);

        tabbedPane.insertTab(start + "Książki" + finish, null, bookGetPanel, null, 0);
        tabbedPane.insertTab(start + "Autorzy" + finish, null, authorGetPanel, null, 1);
        tabbedPane.insertTab(start + "Czytelnicy" + finish, null, readersShowAllPanel, null, 2);
        tabbedPane.insertTab(start + "Wydarzenia" + finish, null, eventGetPanel, null, 3);

        add(tabbedPane);
    }

    private void createLogout(){

        logout = new MyButton(false);
        logout.setText("Wyloguj się");
        logout.setBounds(500,470,200,30);
        add(logout);
    }

    private void createLibrarian(){
        librarianLabel = new JLabel();
        librarianLabel.setBounds(45,470,500,30);
        add(librarianLabel);
    }

    private void action(){
        bookGetPanel.getEdit().addActionListener(e -> {

            if(!bookGetPanel.getResultList().isSelectionEmpty()){

                BookEditPanel bookEditPanel = new BookEditPanel(bookGetPanel);
                tabbedPane.insertTab(start + "Książki" + finish, null, bookEditPanel, null, 0);
                tabbedPane.remove(bookGetPanel);
                tabbedPane.setSelectedIndex(0);

                bookEditPanel.getCancel().addActionListener(e3 -> {
                    tabbedPane.insertTab(start + "Książki" + finish, null, bookGetPanel, null, 0);
                    tabbedPane.remove(bookEditPanel);
                    tabbedPane.setSelectedIndex(0);

                });
            } else {
                JOptionPane.showMessageDialog(this,"Książka nie została wybrana");
            }});

        bookGetPanel.getCreate().addActionListener(e -> {

            BookAddPanel bookAddPanel = new BookAddPanel();

            tabbedPane.insertTab(start + "Książki" + finish, null, bookAddPanel, null, 0);
            tabbedPane.remove(bookGetPanel);
            tabbedPane.setSelectedIndex(0);

            bookAddPanel.getCancel().addActionListener(e3 -> {
                tabbedPane.insertTab(start + "Książki" + finish, null, bookGetPanel, null, 0);
                tabbedPane.remove(bookAddPanel);
                tabbedPane.setSelectedIndex(0);

            });
        });

        readersShowAllPanel.getShowBooks().addActionListener(e -> {

                BookTransferPanel bookTransferPanel = new BookTransferPanel();

                bookTransferPanel.getUserId().setText(String.valueOf(readersShowAllPanel.getReadersCard()));

                tabbedPane.insertTab(start + "Czytelnicy" + finish, null, bookTransferPanel, null, 2);
                tabbedPane.remove(readersShowAllPanel);
                tabbedPane.setSelectedIndex(2);

                bookTransferPanel.getCancel().addActionListener(e3 -> {
                    tabbedPane.insertTab(start + "Czytelnicy" + finish, null, readersShowAllPanel, null, 2);
                    tabbedPane.remove(bookTransferPanel);
                    tabbedPane.setSelectedIndex(2);

                });
        });

        readersShowAllPanel.getShowReader().addActionListener(e -> {

            if(!readersShowAllPanel.getResultList().isSelectionEmpty()){
                ReaderShowDetailPanel readerShowDetailPanel = new ReaderShowDetailPanel(readersShowAllPanel);

                readerShowDetailPanel.getCardIdTxt().setText(String.valueOf(readersShowAllPanel.getReadersCard()));

                tabbedPane.insertTab(start + "Czytelnicy" + finish, null, readerShowDetailPanel, null, 2);
                tabbedPane.remove(readersShowAllPanel);
                tabbedPane.setSelectedIndex(2);

                readerShowDetailPanel.getCancel().addActionListener(e3 -> {
                    tabbedPane.insertTab(start + "Czytelnicy" + finish, null, readersShowAllPanel, null, 2);
                    tabbedPane.remove(readerShowDetailPanel);
                    tabbedPane.setSelectedIndex(2);

                });
            } else {
                JOptionPane.showMessageDialog(this, "Żaden czytelnik nie został wybrany.");
            }
        });

        eventGetPanel.getCreate().addActionListener(e -> {

            EventAddPanel eventAddPanel = new EventAddPanel();

            tabbedPane.insertTab(start + "Wydarzenia" + finish, null, eventAddPanel, null, 3);
            tabbedPane.remove(eventGetPanel);
            tabbedPane.setSelectedIndex(3);

            eventAddPanel.getCancel().addActionListener(e3 -> {
                tabbedPane.insertTab(start + "Wydarzenia" + finish, null, eventGetPanel, null, 3);
                tabbedPane.remove(eventAddPanel);
                tabbedPane.setSelectedIndex(3);

            });
        });

        eventGetPanel.getEdit().addActionListener(e -> {

            if(!eventGetPanel.getResultList().isSelectionEmpty()){

                EventEditPanel eventEditPanel = new EventEditPanel(eventGetPanel);
                tabbedPane.insertTab(start + "Wydarzenia" + finish, null, eventEditPanel, null, 3);
                tabbedPane.remove(eventGetPanel);
                tabbedPane.setSelectedIndex(3);

                eventEditPanel.getCancel().addActionListener(e3 -> {
                    tabbedPane.insertTab(start + "Wydarzenia" + finish, null, eventGetPanel, null, 3);
                    tabbedPane.remove(eventEditPanel);
                    tabbedPane.setSelectedIndex(3);

                });
            } else {
                JOptionPane.showMessageDialog(this,"Wydarzenie nie zostało wybrane.");
            }});
    }

    private void createBackground(){
        BackgroundLabel backgroundLabel = new BackgroundLabel();
        add(backgroundLabel);
    }

    public JLabel getLibrarianLabel(){
        return librarianLabel;
    }

    public MyButton getLogout(){
        return logout;
    }
}

package gui.reader;

import gui.book.*;
import gui.bookTransfer.BookReservePanel;
import gui.bookTransfer.BookShowPanel;
import gui.bookTransfer.BookTransferPanel;
import gui.event.*;
import gui.general.BackgroundLabel;
import gui.general.MyButton;
import gui.libraryinf.LibraryDataShowPanel;
import gui.login.LoginPanel;

import javax.swing.*;

public class ReaderTabbedPanel extends JPanel {

    private ReaderEditPanel readerEditPanel;
    private BookShowPanel bookShowPanel;
    private EventShowPanel eventShowPanel;
    private LibraryDataShowPanel libraryDataShowPanel;
    private JTabbedPane tabbedPane;
    private int idUser;

    private MyButton logout;
    private JLabel readerLabel;
    private String start = "<html><body><table width='137' height='50'>";
    private String finish = "</table></body></html>";

    public ReaderTabbedPanel(LoginPanel loginPanel){

        this.idUser = loginPanel.getUserId();

        setLayout(null);

        createLogout();
        createReader();
        createTabbedPane();
        action();
        createBackground();
    }

    private void createTabbedPane(){

        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(20,20,640,500);

        readerEditPanel = new ReaderEditPanel(this);
        bookShowPanel = new BookShowPanel(this);
        eventShowPanel = new EventShowPanel(this);
        libraryDataShowPanel = new LibraryDataShowPanel();

        tabbedPane.insertTab(start + "Profil" + finish, null, readerEditPanel, null, 0);
        tabbedPane.insertTab(start + "Książki" + finish, null, bookShowPanel, null, 1);
        tabbedPane.insertTab(start + "Wydarzenia" + finish, null, eventShowPanel, null, 2);
        tabbedPane.insertTab(start + "O bibliotece" + finish, null, libraryDataShowPanel, null, 3);

        add(tabbedPane);
    }

    private void createLogout(){

        logout = new MyButton(false);
        logout.setText("Wyloguj się");
        logout.setBounds(500,470,200,30);
        add(logout);
    }

    private void createReader(){
        readerLabel = new JLabel();
        readerLabel.setBounds(45,470,500,30);
        add(readerLabel);
    }

    private void action(){

        bookShowPanel.getSearchBooks().addActionListener(e -> {

                BookReservePanel bookReservePanel = new BookReservePanel(this);
                tabbedPane.insertTab(start + "Książki" + finish, null, bookReservePanel, null, 1);
                tabbedPane.remove(bookShowPanel);
                tabbedPane.setSelectedIndex(1);

                bookReservePanel.getCancel().addActionListener(e3 -> {
                    tabbedPane.insertTab(start + "Książki" + finish, null, bookShowPanel, null, 1);
                    tabbedPane.remove(bookReservePanel);
                    tabbedPane.setSelectedIndex(1);
                });
            });

        eventShowPanel.getSeeDetail().addActionListener(e -> {
            if (!eventShowPanel.getEventsList().isSelectionEmpty()) {

                EventSeeDetailPanel eventSeeDetailPanel = new EventSeeDetailPanel(eventShowPanel);
                tabbedPane.insertTab(start + "Wydarzenia" + finish, null, eventSeeDetailPanel, null, 2);
                tabbedPane.remove(eventShowPanel);
                tabbedPane.setSelectedIndex(2);

                eventSeeDetailPanel.getCancel().addActionListener(e1 -> {
                    tabbedPane.insertTab(start + "Wydarzenia" + finish, null, eventShowPanel, null, 2);
                    tabbedPane.remove(eventSeeDetailPanel);
                    tabbedPane.setSelectedIndex(2);
                });
            }
        });
    }

    private void createBackground(){
        BackgroundLabel backgroundLabel = new BackgroundLabel();
        add(backgroundLabel);
    }

    public JLabel getReaderLabel(){
        return readerLabel;
    }

    public MyButton getLogout(){
        return logout;
    }

    public int getIdUser(){
        return idUser;
    }

}

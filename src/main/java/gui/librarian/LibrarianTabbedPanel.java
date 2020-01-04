package gui.librarian;

import gui.book.*;
import gui.bookTransfer.BookTransferPanel;
import gui.event.EventAddPanel;
import gui.event.EventDeletePanel;
import gui.general.BackgroundLabel;
import gui.general.MyButton;
import gui.reader.ReadersShowAllPanel;
import gui.user.UsersShowAllPanel;

import javax.swing.*;

public class LibrarianTabbedPanel extends JPanel {

    BookGetPanel bookGetPanel = new BookGetPanel();
    AuthorGetPanel authorGetPanel = new AuthorGetPanel();
    EventAddPanel eventAddPanel = new EventAddPanel();
    UsersShowAllPanel usersShowAllPanel = new UsersShowAllPanel();
    JTabbedPane tabbedPane = new JTabbedPane();

    private MyButton logout;

    public LibrarianTabbedPanel(){

        setLayout(null);

        createLogout();
        createTabbedPane();
        action();
        createBackground();

    }

    private void createTabbedPane(){

        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(20,20,640,500);

        tabbedPane.insertTab("<html><body><table width='137'>Książki</table></body></html>", null, bookGetPanel, null, 0);
        tabbedPane.insertTab("<html><body><table width='137'>Autorzy</table></body></html>", null, authorGetPanel, null, 1);
        tabbedPane.insertTab("<html><body><table width='137'>Czytelnicy</table></body></html>", null, usersShowAllPanel, null, 2);
        tabbedPane.insertTab("<html><body><table width='137'>Wydarzenia</table></body></html>", null, eventAddPanel, null, 3);

        add(tabbedPane);
    }

    private void createLogout(){

        logout = new MyButton(false);
        logout.setText("Wyloguj się");
        logout.setBounds(500,470,200,30);
        add(logout);
    }

    private void action(){
        bookGetPanel.getEdit().addActionListener(e -> {

            if(!bookGetPanel.getResultList().isSelectionEmpty()){

                BookEditPanel bookEditPanel = new BookEditPanel(bookGetPanel);
                tabbedPane.insertTab("<html><body><table width='137'>Książki</table></body></html>", null, bookEditPanel, null, 0);
                tabbedPane.remove(bookGetPanel);
                tabbedPane.setSelectedIndex(0);

                bookEditPanel.getCancel().addActionListener(e3 -> {
                    tabbedPane.insertTab("<html><body><table width='137'>Książki</table></body></html>", null, bookGetPanel, null, 0);
                    tabbedPane.remove(bookEditPanel);
                    tabbedPane.setSelectedIndex(0);

                });
            } else {
                JOptionPane.showMessageDialog(this,"Książka nie została wybrana");
            }});

        bookGetPanel.getCreate().addActionListener(e -> {

            BookAddPanel bookAddPanel = new BookAddPanel();

            tabbedPane.insertTab("<html><body><table width='137'>Książki</table></body></html>", null, bookAddPanel, null, 0);
            tabbedPane.remove(bookGetPanel);
            tabbedPane.setSelectedIndex(0);

            bookAddPanel.getCancel().addActionListener(e3 -> {
                tabbedPane.insertTab("<html><body><table width='137'>Książki</table></body></html>", null, bookGetPanel, null, 0);
                tabbedPane.remove(bookAddPanel);
                tabbedPane.setSelectedIndex(0);

            });
        });

        usersShowAllPanel.getShowBooks().addActionListener(e -> {

            if(!usersShowAllPanel.getResultList().isSelectionEmpty()) {

                BookTransferPanel bookTransferPanel = new BookTransferPanel();

                bookTransferPanel.getUserId().setText(String.valueOf(usersShowAllPanel.getUsersBooks()));

                tabbedPane.insertTab("<html><body><table width='137'>Czytelnicy</table></body></html>", null, bookTransferPanel, null, 2);
                tabbedPane.remove(usersShowAllPanel);
                tabbedPane.setSelectedIndex(2);

                bookTransferPanel.getCancel().addActionListener(e3 -> {
                    tabbedPane.insertTab("<html><body><table width='137'>Czytelnicy</table></body></html>", null, usersShowAllPanel, null, 2);
                    tabbedPane.remove(bookTransferPanel);
                    tabbedPane.setSelectedIndex(2);

                });
            }  else {
                JOptionPane.showMessageDialog(this,"Czytelnik nie został wybrany");}
        });
    }

    private void createBackground(){

        BackgroundLabel backgroundLabel = new BackgroundLabel();
        add(backgroundLabel);

    }
}

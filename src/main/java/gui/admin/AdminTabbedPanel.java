package gui.admin;

import gui.general.BackgroundLabel;
import gui.general.LibraryDataPanel;
import gui.general.MyButton;
import gui.librarian.LibrarianAddPanel;
import gui.librarian.LibrarianGetPanel;
import gui.librarian.LibrarianShowPanel;
import gui.reader.ReadersShowAllPanel;

import javax.swing.*;

public class AdminTabbedPanel extends JPanel {

    private LibraryDataPanel libraryDataPanel = new LibraryDataPanel();
    private LibrarianGetPanel librarianGetPanel = new LibrarianGetPanel();
    private AdminGetPanel adminGetPanel = new AdminGetPanel();
    private AdminAddBookshelfPanel adminAddBookshelfPanel = new AdminAddBookshelfPanel();

    private JTabbedPane tabbedPane = new JTabbedPane();

    private MyButton logout;
    private JLabel adminLabel;
    private String start = "<html><body><table width='137' height='50'>";
    private String finish = "</table></body></html>";

    public AdminTabbedPanel(){

        setLayout(null);

        createLogout();
        createAdmin();
        createTabbedPane();
        action();
        createBackground();
    }

    private void createTabbedPane(){

        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(20,20,640,500);
        setBorder(BorderFactory.createDashedBorder(null));
        setOpaque(false);

        tabbedPane.insertTab(start + "Dane o bibliotece" + finish, null, libraryDataPanel, null, 0);
        tabbedPane.insertTab(start + "Bibliotekarze" + finish, null, librarianGetPanel, null, 1);
        tabbedPane.insertTab(start + "Administratorzy" + finish, null, adminGetPanel, null, 2);
        tabbedPane.insertTab(start + "Lokalizacje" + finish, null, adminAddBookshelfPanel, null, 3);

        add(tabbedPane);
    }

    private void createLogout(){

        logout = new MyButton(false);
        logout.setText("Wyloguj się");
        logout.setBounds(500,470,200,30);
        add(logout);
    }

    private void createAdmin(){
        adminLabel = new JLabel();
        adminLabel.setBounds(45,470,500,30);
        add(adminLabel);
    }

    private void action() {
        librarianGetPanel.getShow().addActionListener(e -> {

            if(!librarianGetPanel.getResultList().isSelectionEmpty()){

                LibrarianShowPanel librarianShowPanel = new LibrarianShowPanel(librarianGetPanel);
                librarianShowPanel.getCardIdTxt().setText(String.valueOf(librarianGetPanel.getLibrarianCard()));

                tabbedPane.insertTab(start + "Bibliotekarz" + finish, null, librarianShowPanel, null, 1);
                tabbedPane.remove(librarianGetPanel);
                tabbedPane.setSelectedIndex(1);

                librarianShowPanel.getCancel().addActionListener(e1 -> {
                    tabbedPane.insertTab(start + "Bibliotekarz" + finish, null, librarianGetPanel, null, 1);
                    tabbedPane.remove(librarianShowPanel);
                    tabbedPane.setSelectedIndex(1);

                });
            } else {
                JOptionPane.showMessageDialog(this,"Bibliotekarz nie został wybrany.");
            }});

        librarianGetPanel.getCreate().addActionListener(e -> {
            LibrarianAddPanel librarianAddPanel = new LibrarianAddPanel();
            tabbedPane.insertTab(start + "Bibliotekarz" + finish, null, librarianAddPanel, null, 1);
            tabbedPane.remove(librarianGetPanel);
            tabbedPane.setSelectedIndex(1);

            librarianAddPanel.getReturnBtn().addActionListener(e1 -> {
                tabbedPane.insertTab(start + "Bibliotekarz" + finish, null, librarianGetPanel, null, 1);
                tabbedPane.remove(librarianAddPanel);
                tabbedPane.setSelectedIndex(1);
            });
        });

        adminGetPanel.getShow().addActionListener(e -> {

            if(!adminGetPanel.getResultList().isSelectionEmpty()){

                AdminShowPanel adminShowPanel = new AdminShowPanel(adminGetPanel);
                adminShowPanel.getCardIdTxt().setText(String.valueOf(adminGetPanel.getAdminCard()));

                tabbedPane.insertTab(start + "Administrator" + finish, null, adminShowPanel, null, 2);
                tabbedPane.remove(adminGetPanel);
                tabbedPane.setSelectedIndex(2);

                adminShowPanel.getCancel().addActionListener(e1 -> {
                    tabbedPane.insertTab(start + "Administrator" + finish, null, adminGetPanel, null, 2);
                    tabbedPane.remove(adminShowPanel);
                    tabbedPane.setSelectedIndex(2);

                });
            } else {
                JOptionPane.showMessageDialog(this,"Administrator nie został wybrany.");
            }});

        adminGetPanel.getCreate().addActionListener(e -> {
            AdminAddPanel adminAddPanel = new AdminAddPanel();
            tabbedPane.insertTab(start + "Administrator" + finish, null, adminAddPanel, null, 2);
            tabbedPane.remove(adminGetPanel);
            tabbedPane.setSelectedIndex(2);

            adminAddPanel.getReturnBtn().addActionListener(e1 -> {
                tabbedPane.insertTab(start + "Administrator" + finish, null, adminGetPanel, null, 2);
                tabbedPane.remove(adminAddPanel);
                tabbedPane.setSelectedIndex(2);
            });
        });
    }

    private void createBackground(){
        BackgroundLabel backgroundLabel = new BackgroundLabel();
        add(backgroundLabel);
    }

    public JLabel getAdminLabel(){
        return adminLabel;
    }

    public MyButton getLogout(){
        return logout;
    }
}

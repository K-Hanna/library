package gui;

import config.Validation;
import gui.admin.*;
import gui.book.*;
import gui.bookTransfer.*;
import gui.event.*;
import gui.general.BackgroundPanel;
import gui.librarian.*;
import gui.login.LoginPanel;
import gui.reader.*;
import user.IUserDBService;
import user.User;
import user.UserDBServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MFrame extends JFrame {

    private ReaderAddPanel readerAddPanel;
    private LibrarianAddPanel librarianAddPanel;
    private LibrarianUpdatePanel librarianUpdatePanel;
    private LibrarianDeletePanel librarianDeletePanel;
    private AdminAddPanel adminAddPanel;
    private AdminDeletePanel adminDeletePanel;
    private AdminUpdatePanel adminUpdatePanel;
    private LoginPanel loginPanel;
    private AdminEntryPanel adminEntryPanel;
    private LibrarianTabbedPanel librarianTabbedPanel;
    private ReaderTabbedPanel readerTabbedPanel;

    public MFrame() {

        setSize(700, 600);

        setTitle("Biblioteka");
        setResizable(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        int posX = width / 2 - getWidth() / 2;
        int posY = height / 2 - getHeight() / 2;
        setLocation(posX, posY);



//----------------login panel----------------

        loginPanel = new LoginPanel();
        add(loginPanel);
        addLoginKeyListener();
        addRegisterKeyListener();

        loginPanel.getRegisterBtn().addActionListener(e -> {
            readerAddPanel = new ReaderAddPanel();
            add(readerAddPanel);
            remove(loginPanel);
            repaint();
            revalidate();

            readerAddPanel.getReturnBtn().addActionListener(e1 -> {
                String cardNo = readerAddPanel.getCardIdTxt().getText();
                StringBuilder pass = new StringBuilder();
                for (char c : readerAddPanel.getPassField().getPassword())
                    pass.append(c);
                loginPanel.setCardNrTxt(cardNo);
                loginPanel.setPassTxt(pass.toString());
                add(loginPanel);
                remove(readerAddPanel);
                repaint();
                revalidate();
            });
        });

        loginPanel.getLoginBtn().addActionListener(e -> {
            String cardIdTxt = loginPanel.getCardNrTxt().getText();
            String passTxt = loginPanel.getPasswordToString(loginPanel.getPassTxt());
            if (Validation.checkIfInteger(cardIdTxt)) {
                if (Validation.checkUserExists(Integer.parseInt(cardIdTxt))) {
                    IUserDBService userDBService = new UserDBServiceImpl();
                    User user = userDBService.readUserFromDB(Integer.parseInt(cardIdTxt));
                    loginPanel.setUserId(user.getIdUser());
                    if (Validation.checkPassOk(user, passTxt)) {
                        if (Validation.checkIfReader(user)) {
                            //---------------------CZYTELNIK---------------------------
                            readerTabbedPanel = new ReaderTabbedPanel(loginPanel);
                            readerTabbedPanel.getReaderLabel().setText
                                    ("Zalogowano jako: " + user.getFirstName() + " " + user.getLastName() + ", " + loginPanel.getCardNrTxt().getText());

                            add(readerTabbedPanel);
                            remove(loginPanel);
                            repaint();
                            revalidate();

                            readerTabbedPanel.getLogout().addActionListener(e1 -> {
                                add(loginPanel);
                                remove(readerTabbedPanel);
                                repaint();
                                revalidate();
                            });

                        } else if (Validation.checkIfLibrarian(user))
                        //---------------------BIBILOTEKARZ---------------------------
                        {
                            librarianTabbedPanel = new LibrarianTabbedPanel();
                            librarianTabbedPanel.getLibrarianLabel().setText
                                    ("Zalogowano jako: " + user.getFirstName() + " " + user.getLastName() + ", " + loginPanel.getCardNrTxt().getText());

                            add(librarianTabbedPanel);
                            remove(loginPanel);
                            repaint();
                            revalidate();

                            librarianTabbedPanel.getLogout().addActionListener(e1 -> {
                                add(loginPanel);
                                remove(librarianTabbedPanel);
                                repaint();
                                revalidate();
                            });

                        } else if(Validation.checkIfAdmin(user)){
                            //---------------------ADMINISTRATOR---------------------------
                            adminEntryPanel = new AdminEntryPanel();
                            adminEntryPanel.setCardNrLbl(loginPanel.getCardNrTxt().getText());
                            adminEntryPanel.setNameLbl(user.getFirstName() + " " + user.getLastName());
                            add(adminEntryPanel);
                            remove(loginPanel);
                            repaint();
                            revalidate();
                            //dodawanie bibiliotekarza
                            adminEntryPanel.getAddLibrarianBtn().addActionListener(e1 -> {
                                librarianAddPanel = new LibrarianAddPanel();
                                add(librarianAddPanel);
                                remove(adminEntryPanel);
                                repaint();
                                revalidate();

                                librarianAddPanel.getReturnBtn().addActionListener(e2 -> {
                                    add(adminEntryPanel);
                                    remove(librarianAddPanel);
                                    repaint();
                                    revalidate();
                                });
                            });
                            //usuwanie bibiliotekarza
                            adminEntryPanel.getDeleteLibrarianBtn().addActionListener(e1 -> {
                                librarianDeletePanel = new LibrarianDeletePanel();
                                add(librarianDeletePanel);
                                remove(adminEntryPanel);
                                repaint();
                                revalidate();

                                librarianDeletePanel.getReturnBtn().addActionListener(e2 -> {
                                    add(adminEntryPanel);
                                    remove(librarianDeletePanel);
                                    repaint();
                                    revalidate();
                                });
                            });
                            //update bibliotekarza
                            adminEntryPanel.getUpdateLibrarianBtn().addActionListener(e1 -> {
                                librarianUpdatePanel = new LibrarianUpdatePanel();
                                add(librarianUpdatePanel);
                                remove(adminEntryPanel);
                                repaint();
                                revalidate();

                                librarianUpdatePanel.getReturnBtn().addActionListener(e2 -> {
                                    add(adminEntryPanel);
                                    remove(librarianUpdatePanel);
                                    repaint();
                                    revalidate();
                                });
                            });
                            //dodawanie administratora
                            adminEntryPanel.getAddAdminBtn().addActionListener(e1 -> {
                                adminAddPanel = new AdminAddPanel();
                                add(adminAddPanel);
                                remove(adminEntryPanel);
                                repaint();
                                revalidate();

                                adminAddPanel.getReturnBtn().addActionListener(e2 -> {
                                    add(adminEntryPanel);
                                    remove(adminAddPanel);
                                    repaint();
                                    revalidate();
                                });
                            });
                            //usuwanie administratora
                            adminEntryPanel.getDeleteAdminBtn().addActionListener(e1 -> {
                                adminDeletePanel = new AdminDeletePanel();
                                add(adminDeletePanel);
                                remove(adminEntryPanel);
                                repaint();
                                revalidate();

                                adminDeletePanel.getReturnBtn().addActionListener(e2 -> {
                                    add(adminEntryPanel);
                                    remove(adminDeletePanel);
                                    repaint();
                                    revalidate();
                                });
                            });
                            //update administratora
                            adminEntryPanel.getUpdateAdminBtn().addActionListener(e1 -> {
                                adminUpdatePanel = new AdminUpdatePanel();
                                add(adminUpdatePanel);
                                remove(adminEntryPanel);
                                repaint();
                                revalidate();

                                adminUpdatePanel.getReturnBtn().addActionListener(e2 -> {
                                    add(adminEntryPanel);
                                    remove(adminUpdatePanel);
                                    repaint();
                                    revalidate();
                                });
                            });

                            adminEntryPanel.getReturnBtn().addActionListener(e1 -> {
                                loginPanel.setCardNrTxt("");
                                loginPanel.setPassTxt("");
                                add(loginPanel);
                                remove(adminEntryPanel);
                                repaint();
                                revalidate();
                            });

                        } else
                        {
                            JOptionPane.showMessageDialog(this, "Brak użytkownika w bazie");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Niepoprawnie wprowadzone hasło");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Użytkownik o tym numerze karty nie istnieje w systemie");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Niepoprawny numer karty");
            }
        });
        
//

    }


    private void addLoginKeyListener() {
        loginPanel.getLoginBtn().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    loginPanel.getLoginBtn().doClick();}
            @Override
            public void keyReleased(KeyEvent e) {
            }});
        }

    private void addRegisterKeyListener() {
        loginPanel.getRegisterBtn().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    loginPanel.getRegisterBtn().doClick();}
            @Override
            public void keyReleased(KeyEvent e) {
            }});
    }
}


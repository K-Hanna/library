package gui;

import config.Validation;
import gui.admin.*;
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
    private LoginPanel loginPanel;
    private LibrarianTabbedPanel librarianTabbedPanel;
    private ReaderTabbedPanel readerTabbedPanel;
    private AdminTabbedPanel adminTabbedPanel;

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
                                adminTabbedPanel = new AdminTabbedPanel();
                                adminTabbedPanel.getAdminLabel().setText
                                        ("Zalogowano jako: " + user.getFirstName() + " " + user.getLastName() + ", " + loginPanel.getCardNrTxt().getText());

                                add(adminTabbedPanel);
                                remove(loginPanel);
                                repaint();
                                revalidate();

                                adminTabbedPanel.getLogout().addActionListener(e1 -> {
                                    add(loginPanel);
                                    remove(adminTabbedPanel);
                                    repaint();
                                    revalidate();
                                });

                            }  else  {
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


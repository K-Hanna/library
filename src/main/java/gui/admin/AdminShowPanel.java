package gui.admin;

import admin.Admin;
import admin.AdminDBServiceImpl;
import admin.IAdminDBService;
import card.CardDBServiceImpl;
import card.ICardDBService;
import city.CityDBServiceImpl;
import city.ICityDBService;
import config.Validation;
import gui.general.MyButton;
import images.IPosterDBService;
import images.Poster;
import images.PosterDBServiceImpl;
import librarian.ILibrarianDBService;
import librarian.Librarian;
import librarian.LibrarianDBServiceImpl;
import user.IUserDBService;
import user.User;
import user.UserDBServiceImpl;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AdminShowPanel extends JPanel {

    private JLabel firstNameLbl, lastNamelbl, emailLbl, passLbl, cardIdLbl, postalCodeLbl, cityNameLbl, streetAndBuildingLbl;
    private JLabel salaryLbl, isFullTimeLbl;
    private JTextField firstNameTxt, lastNameTxt, emailTxt, cardIdTxt, postalCodeTxt, cityNameTxt, streetAndBuildingTxt;
    private JTextField salaryTxt;
    private JPasswordField passTxt;
    private JCheckBox isFullTimeChbx;
    private JButton edit, confirm, cancel;
    private int fieldLength = 200, adminCard;

    private ICityDBService cityDBService = new CityDBServiceImpl();
    private IAdminDBService adminDBService = new AdminDBServiceImpl();
    private IUserDBService userDBService = new UserDBServiceImpl();

    private Admin admin;

    public AdminShowPanel(AdminGetPanel adminGetPanel){

        this.adminCard = adminGetPanel.getAdminCard();
        admin = adminDBService.readAdminFromDB(adminCard);

        setLayout(null);

        createButtons();
        createFields();
        addComps();
        setComponentsEditability(false);
        setPostalCodeKL();
        action();

    }

    private void setPostalCodeKL() {
        postalCodeTxt.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {cityNameTxt.setText(cityDBService.getCityName(postalCodeTxt.getText())); }
        });
    }

    private void createButtons(){

        confirm = new MyButton(true);
        confirm.setText("Aktualizuj dane");
        confirm.setVisible(false);
        confirm.setBounds(400, 20, 200, 30);

        cancel = new MyButton(false);
        cancel.setText("Powrót");
        cancel.setBounds(400, 60, 200, 30);

        edit = new MyButton(true);
        edit.setText("Zmień dane");
        edit.setBounds(400,20,200,30);
    }

    private void createFields(){

        cardIdLbl = new JLabel();
        cardIdLbl.setText("Numer karty");
        cardIdLbl.setBounds(50, 20, 100, 30);

        cardIdTxt = new JTextField();
        cardIdTxt.setBounds(150, 20, fieldLength, 30);
        cardIdTxt.setEditable(false);
        cardIdTxt.setText(String.valueOf(admin.getCardNumber()));

        firstNameLbl = new JLabel();
        firstNameLbl.setText("Imię");
        firstNameLbl.setBounds(50, 60, 100, 30);

        firstNameTxt = new JTextField();
        firstNameTxt.setBounds(150, 60, fieldLength, 30);
        firstNameTxt.setText(admin.getFirstName());

        lastNamelbl = new JLabel();
        lastNamelbl.setText("Nazwisko");
        lastNamelbl.setBounds(50, 100, 100, 30);

        lastNameTxt = new JTextField();
        lastNameTxt.setBounds(150, 100, fieldLength, 30);
        lastNameTxt.setText(admin.getLastName());

        emailLbl = new JLabel();
        emailLbl.setText("Email");
        emailLbl.setBounds(50, 140, 100, 30);

        emailTxt = new JTextField();
        emailTxt.setBounds(150, 140, fieldLength, 30);
        emailTxt.setText(admin.getEmail());

        streetAndBuildingLbl = new JLabel();
        streetAndBuildingLbl.setText("Ulica/nr");
        streetAndBuildingLbl.setBounds(50, 180, 100, 30);

        streetAndBuildingTxt = new JTextField();
        streetAndBuildingTxt.setBounds(150, 180, fieldLength, 30);
        streetAndBuildingTxt.setText(admin.getStreetBuilding());

        postalCodeLbl = new JLabel();
        postalCodeLbl.setText("Kod pocztowy");
        postalCodeLbl.setBounds(50, 220, 100, 30);

        postalCodeTxt = new JTextField();
        postalCodeTxt.setBounds(150, 220, fieldLength, 30);
        postalCodeTxt.setText(admin.getPostalCode());

        cityNameLbl = new JLabel();
        cityNameLbl.setText("Miasto");
        cityNameLbl.setBounds(50, 260, 100, 30);

        cityNameTxt = new JTextField();
        cityNameTxt.setBounds(150, 260, fieldLength, 30);
        cityNameTxt.setText(cityDBService.getCityName(postalCodeTxt.getText()));
        cityNameTxt.setEditable(false);

        passLbl = new JLabel();
        passLbl.setText("Hasło");
        passLbl.setBounds(50, 300, 100, 30);

        passTxt = new JPasswordField();
        passTxt.setText(admin.getPassword());
        passTxt.setBounds(150, 300, fieldLength, 30);

        salaryLbl = new JLabel();
        salaryLbl.setText("Pensja");
        salaryLbl.setBounds(50, 340, 100, 30);

        salaryTxt = new JTextField();
        salaryTxt.setBounds(150, 340, fieldLength, 30);
        salaryTxt.setText(admin.getSalary());

        isFullTimeLbl = new JLabel();
        isFullTimeLbl.setText("Pełny etat");
        isFullTimeLbl.setBounds(50, 380, 100, 30);

        isFullTimeChbx = new JCheckBox();
        isFullTimeChbx.setEnabled(false);
        isFullTimeChbx.setBounds(150, 380, 30, 30);
        isFullTimeChbx.setSelected(admin.isFullTime());

    }

    private void addComps() {
        add(cardIdLbl);
        add(cardIdTxt);
        add(firstNameLbl);
        add(firstNameTxt);
        add(lastNamelbl);
        add(lastNameTxt);
        add(emailLbl);
        add(emailTxt);
        add(postalCodeLbl);
        add(postalCodeTxt);
        add(cityNameLbl);
        add(cityNameTxt);
        add(streetAndBuildingLbl);
        add(streetAndBuildingTxt);
        add(passLbl);
        add(passTxt);
        add(salaryLbl);
        add(salaryTxt);
        add(isFullTimeLbl);
        add(isFullTimeChbx);
        add(edit);
        add(confirm);
        add(cancel);
    }

    private void action() {

        edit.addActionListener(e -> {
            setComponentsEditability(true);
            confirm.setVisible(true);
            edit.setVisible(false);
            cancel.setText("Anuluj");
        });

        cancel.addActionListener(e ->{
            setComponentsEditability(false);
            confirm.setVisible(false);
            edit.setVisible(true);
        });

        confirm.addActionListener(e -> {
            if(Validation.checkIfEmailOK(emailTxt.getText()) == false)
                JOptionPane.showMessageDialog(this, "Niepoprawny email");
            else if(Validation.checkIfPostalCodeOK(cityNameTxt.getText())==false)
                JOptionPane.showMessageDialog(this, "Niepoprawny kod pocztowy");
            else if(Validation.checkIfInteger(cardIdTxt.getText()) == false)
                JOptionPane.showMessageDialog(this, "Niepoprawny numer karty użytkownika");
            else if(firstNameTxt.getText().equals("") || lastNameTxt.getText().equals("")|| emailTxt.getText().equals("")||postalCodeTxt.getText().equals("")||streetAndBuildingTxt.getText().equals(""))
                JOptionPane.showMessageDialog(this, "Proszę wypełnić wszystkie pola");
            else {
                int cardId = Integer.parseInt(cardIdTxt.getText());
                User user = userDBService.readUserFromDB(cardId);
                int userId = user.getIdUser();
                String userFirstName = firstNameTxt.getText();
                String userLastName = lastNameTxt.getText();
                String userEmail = emailTxt.getText();
                String userSteetBuilding = streetAndBuildingTxt.getText();
                String userPostalCode = postalCodeTxt.getText();
                StringBuilder pass = new StringBuilder();
                for (char c : passTxt.getPassword())
                    pass.append(c);
                String userPass;
                if (pass.toString().equals("------"))
                    userPass = user.getPassword();
                else
                    userPass = pass.toString();

                userDBService.updateUserInDB(userId, userFirstName, userLastName, userEmail, userPass, userSteetBuilding, userPostalCode, cardId);
                adminDBService.updateAdminInDB(admin.getUserId(), salaryTxt.getText(), isFullTimeChbx.isSelected());
                setComponentsEditability(false);
                confirm.setVisible(false);
                cancel.setText("Powrót");
                edit.setVisible(true);
                JOptionPane.showMessageDialog(this, "Dane użytkownika zaktualizowane poprawnie");
            }
        });
    }

    private void setComponentsEditability(boolean editability){

        firstNameTxt.setEditable(editability);
        lastNameTxt.setEditable(editability);
        emailTxt.setEditable(editability);
        passTxt.setEnabled(editability);
        cardIdTxt.setEnabled(editability);
        postalCodeTxt.setEditable(editability);
        cityNameTxt.setEditable(editability);
        streetAndBuildingTxt.setEditable(editability);
        salaryTxt.setEditable(editability);
        isFullTimeChbx.setEnabled(editability);
    }

    public JButton getCancel(){
        return cancel;
    }

    public JTextField getCardIdTxt(){return cardIdTxt;}
}

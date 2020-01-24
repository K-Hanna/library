package gui.reader;

import card.Card;
import card.CardDBServiceImpl;
import card.ICardDBService;
import city.CityDBServiceImpl;
import city.ICityDBService;
import config.Validation;
import gui.general.BackgroundLabel;
import gui.general.MyButton;
import gui.general.MyLabel;
import reader.IReaderDBService;
import reader.ReaderDBServiceImpl;
import user.IUserDBService;
import user.User;
import user.UserDBServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ReaderAddPanel  extends JPanel {
    private JLabel firstNameLbl, lastNamelbl, emailLbl, passLbl, cardIdLbl, postalCodeLbl, cityNameLbl, streetAndBuildingLbl;
    private JTextField firstNameTxt, lastNameTxt, emailTxt, cardIdTxt, postalCodeTxt, cityNameTxt, streetAndBuildingTxt;
    private JPasswordField passField;
    private MyButton addReaderBtn, returnBtn;
    private int fieldLength = 200;
    private String message;
    private JTextField result;
    private JLabel count;

    private int a, b;

    private IUserDBService userDBService = new UserDBServiceImpl();
    private ICityDBService cityDBService = new CityDBServiceImpl();
    private IReaderDBService readerDBService = new ReaderDBServiceImpl();

    public ReaderAddPanel(){
        setLayout(null);

        a = (int) (Math.random() * 10);
        b = (int) (Math.random() * 10);

        createAllLabels();
        addAllLabels();
        cardIdTxt.setVisible(false);
        cardIdLbl.setVisible(false);
        createReturnBtn();
        add(returnBtn);
        createAddBtn();
        add(addReaderBtn);
        setPostalCodeKL();
        actionAddReaderBtn();
        createBackground();
    }
    private void setPostalCodeKL() {
        postalCodeTxt.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }
            @Override
            public void keyPressed(KeyEvent e) { }
            @Override
            public void keyReleased(KeyEvent e) { cityNameTxt.setText(cityDBService.getCityName(postalCodeTxt.getText())); }
        });
    }

    private void actionAddReaderBtn() {
        addReaderBtn.addActionListener(e -> {

            if (!check()){
                JOptionPane.showMessageDialog(this, message);
            } else {
                ICardDBService cardDBService = new CardDBServiceImpl();
                cardDBService.addCardInDB();
                Card cardForNewUser = cardDBService.readLastCardFromDB();
                cardIdTxt.setText(String.valueOf(cardForNewUser.getIdCard()));
                cardIdTxt.setVisible(true);
                cardIdLbl.setVisible(true);
                User user = new User();
                user.setCardNumber(cardForNewUser.getIdCard());
                user.setFirstName(firstNameTxt.getText());
                user.setLastName(lastNameTxt.getText());
                StringBuilder pass = new StringBuilder();
                for (char c : passField.getPassword())
                    pass.append(c);
                String userPass;
                if (pass.toString().equals("------"))
                    userPass = user.getPassword();
                else
                    userPass = pass.toString();
                user.setPassword(userPass);
                user.setEmail(emailTxt.getText());
                user.setStreetBuilding(streetAndBuildingTxt.getText());
                user.setPostalCode(postalCodeTxt.getText());
                userDBService.addUserInDB(user);
                int idNewReader = userDBService.readUserFromDB(cardForNewUser.getIdCard()).getIdUser();
                readerDBService.addReaderInDB(idNewReader);
                JOptionPane.showMessageDialog(this, "Nowy czytelnik został dodany do bazy \nNumer karty: " + cardForNewUser.getIdCard());
                setComponentsEditability();
                cardIdTxt.setEditable(false);
            }
        });
    }

    private void createReturnBtn() {
        returnBtn = new MyButton(false);
        returnBtn.setText("Anuluj");
        returnBtn.setBounds(350, 380, 150, 30);
    }

    private void createAddBtn() {
        addReaderBtn = new MyButton(true);
        addReaderBtn.setText("Załóż konto");
        addReaderBtn.setBounds(200, 380, 150, 30);
    }

    private void addAllLabels() {
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
        add(passField);
        add(count);
        add(result);
    }

    private void createAllLabels() {
        createCardidLbl();
        createCardIdTxt();
        createFirstnameLbl();
        createFirstNameTxt();
        createLastnameLbl();
        createLastNameTxt();
        createEmailLbl();
        createEmailTxt();
        createPostalCodeLbl();
        createPostalCodeTxt();
        createCityNameLbl();
        createCityNameTxt();
        createStreetAndBuildingLbl();
        createStreetAndBuildingTxt();
        createPassLbl();
        createPassTxt();
        createCaptcha();
    }

    private void createPassLbl() {
        passLbl = new JLabel();
        passLbl.setText("Hasło:");
        passLbl.setBounds(200, 300, 100, 30);
    }

    private void createPassTxt() {
        passField = new JPasswordField();
        passField.setBounds(300, 300, fieldLength, 30);
    }

    private void createStreetAndBuildingLbl() {
        streetAndBuildingLbl = new JLabel();
        streetAndBuildingLbl.setText("Ulica/nr:");
        streetAndBuildingLbl.setBounds(200, 260, 100, 30);
    }

    private void createStreetAndBuildingTxt() {
        streetAndBuildingTxt = new JTextField();
        streetAndBuildingTxt.setBounds(300, 260, fieldLength, 30);
    }

    private void createCityNameLbl() {
        cityNameLbl = new JLabel();
        cityNameLbl.setText("Miasto:");
        cityNameLbl.setBounds(200, 220, 100, 30);
    }

    private void createCityNameTxt() {
        cityNameTxt = new JTextField();
        cityNameTxt.setBounds(300, 220, fieldLength, 30);
        cityNameTxt.setEditable(false);
        cityNameTxt.setBackground(Color.white);
        cityNameTxt.setBorder(BorderFactory.createLineBorder(Color.black));
        cityNameTxt.setOpaque(true);
    }

    private void createPostalCodeLbl() {
        postalCodeLbl = new JLabel();
        postalCodeLbl.setText("Kod pocztowy:");
        postalCodeLbl.setBounds(200, 180, 100, 30);
    }

    private void createPostalCodeTxt() {
        postalCodeTxt = new JTextField();
        postalCodeTxt.setBounds(300, 180, fieldLength, 30);
    }

    private void createEmailLbl() {
        emailLbl = new JLabel();
        emailLbl.setText("Email:");
        emailLbl.setBounds(200, 140, 100, 30);
    }

    private void createEmailTxt() {
        emailTxt = new JTextField();
        emailTxt.setBounds(300, 140, fieldLength, 30);
    }

    private void createLastnameLbl() {
        lastNamelbl = new JLabel();
        lastNamelbl.setText("Nazwisko:");
        lastNamelbl.setBounds(200, 100, 100, 30);
    }

    private void createLastNameTxt() {
        lastNameTxt = new JTextField();
        lastNameTxt.setBounds(300, 100, fieldLength, 30);
    }

    private void createFirstnameLbl() {
        firstNameLbl = new JLabel();
        firstNameLbl.setText("Imię:");
        firstNameLbl.setBounds(200, 60, 100, 30);
    }

    private void createFirstNameTxt() {
        firstNameTxt = new JTextField();
        firstNameTxt.setBounds(300, 60, fieldLength, 30);
    }

    private void createCardidLbl() {
        cardIdLbl = new JLabel();
        cardIdLbl.setText("Numer karty:");
        cardIdLbl.setBounds(200, 20, 100, 30);
    }

    private void createCardIdTxt() {
        cardIdTxt = new JTextField();
        cardIdTxt.setBounds(300, 20, fieldLength, 30);
    }

    private void setComponentsEditability() {

        firstNameTxt.setEditable(false);
        lastNameTxt.setEditable(false);
        emailTxt.setEditable(false);
        passField.setEditable(false);
        cardIdTxt.setEditable(false);
        postalCodeTxt.setEditable(false);
        cityNameTxt.setEditable(false);
        streetAndBuildingTxt.setEditable(false);
        result.setEditable(false);
    }

    private void createCaptcha(){

        count = new JLabel(a +" + " + b);
        count.setBounds(200,340,100,30);

        result = new JTextField();
        result.setBounds(300,340,100,30);

    }

    private void createBackground(){

        MyLabel myLabel = new MyLabel();
        myLabel.setBounds(150,20,400,450);

        BackgroundLabel backgroundLabel = new BackgroundLabel();
        backgroundLabel.setBounds(0,0,700,600);

        add(myLabel);
        add(backgroundLabel);

    }

    private boolean check(){

        message = "Nieprawidłowo wypełnione pola:";

        boolean emailCorrect = Validation.checkIfEmailOK(emailTxt.getText());
        if(!emailCorrect)
            message = message + "\n- nieprawidłowy e-mail";
        boolean postalCorrect = Validation.checkIfPostalCodeOK(cityNameTxt.getText());
        if(!postalCorrect)
            message = message + "\n- nieprawidłowy kod pocztowy";
        boolean empty = firstNameTxt.getText().equals("") || lastNameTxt.getText().equals("") || emailTxt.getText().equals("") || postalCodeTxt.getText().equals("") || streetAndBuildingTxt.getText().equals("");
        if(empty)
            message = message + "\n- brakujące dane";

        if(!captcha())
            message = message + "\n- brakujące lub niepoprawne captcha.";

        return emailCorrect && postalCorrect && !empty && captcha();
    }

    private boolean captcha(){

        if(result.getText().isEmpty())
            return false;

        if(result.getText().matches("[0-9]+")){
            return Integer.parseInt(result.getText()) == a + b;
        } else {
            return false;
        }
    }

    public MyButton getReturnBtn() {
        return returnBtn;
    }
    public JTextField getCardIdTxt(){return cardIdTxt;}
    public JPasswordField getPassField(){return passField;}


}



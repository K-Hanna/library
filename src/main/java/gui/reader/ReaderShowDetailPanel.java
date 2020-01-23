package gui.reader;

import city.CityDBServiceImpl;
import city.ICityDBService;
import gui.general.MyButton;
import images.IPosterDBService;
import images.Poster;
import images.PosterDBServiceImpl;
import user.IUserDBService;
import user.User;
import user.UserDBServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ReaderShowDetailPanel extends JPanel {

    private JLabel firstNameLbl, lastNamelbl, emailLbl, passLbl, cardIdLbl, postalCodeLbl, cityNameLbl, streetAndBuildingLbl;
    private JTextField firstNameTxt, lastNameTxt, emailTxt, cardIdTxt, postalCodeTxt, cityNameTxt, streetAndBuildingTxt;
    private JTextField passField;
    private JLabel photo;
    private MyButton cancel;
    private int fieldLength = 200, cardUser;

    private IUserDBService userDBService = new UserDBServiceImpl();
    private ICityDBService cityDBService = new CityDBServiceImpl();
    private IPosterDBService posterDBService = new PosterDBServiceImpl();

    private User user;

    public ReaderShowDetailPanel(ReadersShowAllPanel readersShowAllPanel) {

        this.cardUser = readersShowAllPanel.getReadersCard();
        user = userDBService.readUserFromDB(cardUser);

        setLayout(null);

        createButtons();
        createFields();
        addComps();
        setComponentsEditability(false);
        setPostalCodeKL();

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

        cancel = new MyButton(false);
        cancel.setText("Powrót");
        cancel.setBounds(400, 300, 200, 30);

    }

    private void createFields(){

        cardIdLbl = new JLabel();
        cardIdLbl.setText("Numer karty");
        cardIdLbl.setBounds(50, 20, 100, 30);

        cardIdTxt = new JTextField();
        cardIdTxt.setBounds(150, 20, fieldLength, 30);
        cardIdTxt.setText(String.valueOf(user.getCardNumber()));

        firstNameLbl = new JLabel();
        firstNameLbl.setText("Imię");
        firstNameLbl.setBounds(50, 60, 100, 30);

        firstNameTxt = new JTextField();
        firstNameTxt.setBounds(150, 60, fieldLength, 30);
        firstNameTxt.setText(user.getFirstName());

        lastNamelbl = new JLabel();
        lastNamelbl.setText("Nazwisko");
        lastNamelbl.setBounds(50, 100, 100, 30);

        lastNameTxt = new JTextField();
        lastNameTxt.setBounds(150, 100, fieldLength, 30);
        lastNameTxt.setText(user.getLastName());

        emailLbl = new JLabel();
        emailLbl.setText("Email");
        emailLbl.setBounds(50, 140, 100, 30);

        emailTxt = new JTextField();
        emailTxt.setBounds(150, 140, fieldLength, 30);
        emailTxt.setText(user.getEmail());

        streetAndBuildingLbl = new JLabel();
        streetAndBuildingLbl.setText("Ulica/nr");
        streetAndBuildingLbl.setBounds(50, 180, 100, 30);

        streetAndBuildingTxt = new JTextField();
        streetAndBuildingTxt.setBounds(150, 180, fieldLength, 30);
        streetAndBuildingTxt.setText(user.getStreetBuilding());

        postalCodeLbl = new JLabel();
        postalCodeLbl.setText("Kod pocztowy");
        postalCodeLbl.setBounds(50, 220, 100, 30);

        postalCodeTxt = new JTextField();
        postalCodeTxt.setBounds(150, 220, fieldLength, 30);
        postalCodeTxt.setText(user.getPostalCode());

        cityNameLbl = new JLabel();
        cityNameLbl.setText("Miasto");
        cityNameLbl.setBounds(50, 260, 100, 30);

        cityNameTxt = new JTextField();
        cityNameTxt.setBounds(150, 260, fieldLength, 30);
        cityNameTxt.setText(cityDBService.getCityName(postalCodeTxt.getText()));

        passLbl = new JLabel();
        passLbl.setText("Hasło");
        passLbl.setBounds(50, 300, 100, 30);

        passField = new JTextField();
        passField.setText(user.getPassword());
        passField.setBounds(150, 300, fieldLength, 30);

        photo = new JLabel();
        photo.setBounds(400,20,200,200);
        photo.setBorder(BorderFactory.createLineBorder(Color.black));

        if(user.getPhoto() > 0) {
            Poster posterId = posterDBService.readImageById(user.getPhoto());
            ImageIcon icon = new ImageIcon(posterId.getImgBytes());
            photo.setIcon(icon);
        }

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
        add(passField);
        add(photo);
        add(cancel);
    }

    private void setComponentsEditability(boolean editability) {
        cardIdTxt.setEditable(editability);
        cardIdTxt.setBorder(null);
        firstNameTxt.setEditable(editability);
        firstNameTxt.setBorder(null);
        lastNameTxt.setEditable(editability);
        lastNameTxt.setBorder(null);
        emailTxt.setEditable(editability);
        emailTxt.setBorder(null);
        streetAndBuildingTxt.setEditable(editability);
        streetAndBuildingTxt.setBorder(null);
        postalCodeTxt.setEditable(editability);
        postalCodeTxt.setBorder(null);
        cityNameTxt.setEditable(editability);
        cityNameTxt.setBorder(null);
        passField.setEditable(editability);
        passField.setBorder(null);
    }

    public JTextField getCardIdTxt(){return cardIdTxt;}

    public MyButton getCancel(){
        return cancel;
    }

}

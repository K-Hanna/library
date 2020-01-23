package gui.reader;

import city.CityDBServiceImpl;
import city.ICityDBService;
import config.Validation;
import gui.event.ImageFilter;
import gui.general.MyButton;
import images.IPosterDBService;
import images.Poster;
import images.PosterDBServiceImpl;
import user.IUserDBService;
import user.User;
import user.UserDBServiceImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class ReaderEditPanel extends JPanel {

    private JLabel firstNameLbl, lastNamelbl, emailLbl, passLbl, cardIdLbl, postalCodeLbl, cityNameLbl, streetAndBuildingLbl;
    private JTextField firstNameTxt, lastNameTxt, emailTxt, cardIdTxt, postalCodeTxt, cityNameTxt, streetAndBuildingTxt;
    private JPasswordField passField;
    private MyButton edit, confirm, cancel, getPhoto;
    private JLabel photoLbl;
    private JTextField photoPath;
    private JFileChooser fileChooser;
    private int fieldLength = 200, idUser;

    private IUserDBService userDBService = new UserDBServiceImpl();
    private ICityDBService cityDBService = new CityDBServiceImpl();
    private IPosterDBService posterDBService = new PosterDBServiceImpl();

    private User user;

    public ReaderEditPanel(ReaderTabbedPanel readerTabbedPanel) {

        this.idUser = readerTabbedPanel.getIdUser();

        user = userDBService.readUserFromDBById(idUser);

        setLayout(null);

        createButtons();
        createFields();
        setTextFields();
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

    private void action() {

        edit.addActionListener(e -> {
            setComponentsEditability(true);
            confirm.setVisible(true);
            edit.setVisible(false);
            cancel.setVisible(true);
            getPhoto.setVisible(true);
        });

        cancel.addActionListener(e ->{
            setComponentsEditability(false);
            setTextFields();
            confirm.setVisible(false);
            cancel.setVisible(false);
            edit.setVisible(true);
            getPhoto.setVisible(false);
        });

        confirm.addActionListener(e -> {
            if(!Validation.checkIfEmailOK(emailTxt.getText()))
                JOptionPane.showMessageDialog(this, "Niepoprawny email");
            else if(!Validation.checkIfPostalCodeOK(cityNameTxt.getText()))
                JOptionPane.showMessageDialog(this, "Niepoprawny kod pocztowy");
            else if(!Validation.checkIfInteger(cardIdTxt.getText()))
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
                for (char c : passField.getPassword())
                    pass.append(c);
                String userPass;
                if (pass.toString().equals("------"))
                    userPass = user.getPassword();
                else
                    userPass = pass.toString();

                userDBService.updateUserInDB(userId, userFirstName, userLastName, userEmail, userPass, userSteetBuilding, userPostalCode, cardId);

                if(!photoPath.getText().isEmpty()){
                    posterDBService.addImage(getPhotoPath().getText());
                    Poster posterForNewEvent = posterDBService.readLastImageFromDB();
                    int newPosterId = posterForNewEvent.getIdImg();
                    userDBService.setUserPhoto(newPosterId, cardId);
                }

                setComponentsEditability(false);
                confirm.setVisible(false);
                getPhoto.setVisible(false);
                edit.setVisible(true);
                cancel.setVisible(false);
                JOptionPane.showMessageDialog(this, "Dane użytkownika zaktualizowane poprawnie.");
            }
        });

        getPhoto.addActionListener(e -> {
            fileChooser = new JFileChooser("C:\\Users\\e495405\\Desktop\\Baza danych zdjęcia\\biblio\\postery\\200_300");
            fileChooser.addChoosableFileFilter(new ImageFilter());
            fileChooser.setAcceptAllFileFilterUsed(false);
            int r = fileChooser.showOpenDialog(this);
            if(r == JFileChooser.APPROVE_OPTION){
                getPhotoPath().setText(fileChooser.getSelectedFile().getAbsolutePath());
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    photoLbl.setIcon(new ImageIcon(ImageIO.read(file)));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else
                getPhotoPath().setText("");
        });

    }

    private void createButtons(){

        confirm = new MyButton(true);
        confirm.setText("Aktualizuj dane");
        confirm.setVisible(false);
        confirm.setBounds(400, 300, 200, 30);

        cancel = new MyButton(false);
        cancel.setText("Anuluj");
        cancel.setBounds(400, 340, 200, 30);
        cancel.setVisible(false);

        edit = new MyButton(true);
        edit.setText("Zmień dane");
        edit.setBounds(400,300,200,30);

        getPhoto = new MyButton(true);
        getPhoto.setText("Wybierz zdjęcie");
        getPhoto.setBounds(400,230,200,30);
        getPhoto.setVisible(false);
    }

    private void createFields(){

        cardIdLbl = new JLabel();
        cardIdLbl.setText("Numer karty");
        cardIdLbl.setBounds(50, 20, 100, 30);

        cardIdTxt = new JTextField();
        cardIdTxt.setBounds(150, 20, fieldLength, 30);
        cardIdTxt.setEditable(false);
        cardIdTxt.setText(String.valueOf(user.getCardNumber()));

        firstNameLbl = new JLabel();
        firstNameLbl.setText("Imię");
        firstNameLbl.setBounds(50, 60, 100, 30);

        firstNameTxt = new JTextField();
        firstNameTxt.setBounds(150, 60, fieldLength, 30);

        lastNamelbl = new JLabel();
        lastNamelbl.setText("Nazwisko");
        lastNamelbl.setBounds(50, 100, 100, 30);

        lastNameTxt = new JTextField();
        lastNameTxt.setBounds(150, 100, fieldLength, 30);

        emailLbl = new JLabel();
        emailLbl.setText("Email");
        emailLbl.setBounds(50, 140, 100, 30);

        emailTxt = new JTextField();
        emailTxt.setBounds(150, 140, fieldLength, 30);

        streetAndBuildingLbl = new JLabel();
        streetAndBuildingLbl.setText("Ulica/nr");
        streetAndBuildingLbl.setBounds(50, 180, 100, 30);

        streetAndBuildingTxt = new JTextField();
        streetAndBuildingTxt.setBounds(150, 180, fieldLength, 30);

        postalCodeLbl = new JLabel();
        postalCodeLbl.setText("Kod pocztowy");
        postalCodeLbl.setBounds(50, 220, 100, 30);

        postalCodeTxt = new JTextField();
        postalCodeTxt.setBounds(150, 220, fieldLength, 30);

        cityNameLbl = new JLabel();
        cityNameLbl.setText("Miasto");
        cityNameLbl.setBounds(50, 260, 100, 30);

        cityNameTxt = new JTextField();
        cityNameTxt.setBounds(150, 260, fieldLength, 30);
        cityNameTxt.setEditable(false);

        passLbl = new JLabel();
        passLbl.setText("Hasło");
        passLbl.setBounds(50, 300, 100, 30);

        passField = new JPasswordField();
        passField.setBounds(150, 300, fieldLength, 30);

        photoPath = new JTextField();
        photoPath.setBounds(150,340,200,30);
        photoPath.setVisible(false);

        photoLbl = new JLabel();
        photoLbl.setBounds(400,20,200,200);
        photoLbl.setBorder(BorderFactory.createLineBorder(Color.black));

        if(user.getPhoto() > 0) {
            Poster posterId = posterDBService.readImageById(user.getPhoto());
            ImageIcon icon = new ImageIcon(posterId.getImgBytes());
            photoLbl.setIcon(icon);
        }
    }

    private void setTextFields(){
        firstNameTxt.setText(user.getFirstName());
        lastNameTxt.setText(user.getLastName());
        emailTxt.setText(user.getEmail());
        streetAndBuildingTxt.setText(user.getStreetBuilding());
        postalCodeTxt.setText(user.getPostalCode());
        cityNameTxt.setText(cityDBService.getCityName(postalCodeTxt.getText()));
        passField.setText(user.getPassword());
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
        add(edit);
        add(confirm);
        add(cancel);
        add(photoPath);
        add(photoLbl);
        add(getPhoto);
    }

    private void setComponentsEditability(boolean editability) {
        firstNameTxt.setEditable(editability);
        lastNameTxt.setEditable(editability);
        emailTxt.setEditable(editability);
        streetAndBuildingTxt.setEditable(editability);
        postalCodeTxt.setEditable(editability);
        passField.setEditable(editability);
    }

    public JTextField getCardIdTxt(){return cardIdTxt;}

    public JTextField getPhotoPath(){
        return photoPath;
    }

}

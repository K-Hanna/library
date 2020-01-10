package gui.general;

import city.CityDBServiceImpl;
import city.ICityDBService;
import config.Validation;
import libraryData.ILibraryData;
import libraryData.LibraryData;
import libraryData.LibraryDataService;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LibraryDataPanel extends JPanel {

    private JLabel nameLbl, addressLbl, postalCodeLbl, cityLbl, openHoursLbl, openDaysLbl;
    private JTextField name, address, postalCode, city, openHours, openDays;
    private MyButton edit, confirm, cancel;
    private int fieldLength = 200, fieldHeight = 30, labelLength = 100;

    private ILibraryData libraryDataService = new LibraryDataService();
    private ICityDBService cityDBService = new CityDBServiceImpl();
    private LibraryData libraryData;

    public LibraryDataPanel(){

        libraryData = libraryDataService.getLibraryDatas();

        setLayout(null);

        createLabels();
        createFields();
        setTextFields();
        addFields();
        createButtons();
        setEditable(false);
        setPostalCodeKL();
        actions();
    }

    private void createLabels() {

        JLabel[] labels = {nameLbl, addressLbl, postalCodeLbl, cityLbl, openHoursLbl, openDaysLbl};
        String[] labelNames = {"Nazwa:", "Adres:", "Kod pocztowy:", "Miasto:", "Godziny otwarcia:", "Dni otwarcia:"};

        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel();
            labels[i].setBounds(50, 20 + 40 * i, labelLength, fieldHeight);
            labels[i].setText(labelNames[i]);
            add(labels[i]);
        }
    }

    private void createFields() {

        name = new JTextField();
        name.setBounds(150, 20, fieldLength, fieldHeight);

        address = new JTextField();
        address.setBounds(150, 60, fieldLength, fieldHeight);

        postalCode = new JTextField();
        postalCode.setBounds(150, 100, fieldLength, fieldHeight);

        city = new JTextField();
        city.setBounds(150, 140, fieldLength, fieldHeight);
        city.setEditable(false);

        openHours = new JTextField();
        openHours.setBounds(150, 180, fieldLength, fieldHeight);

        openDays = new JTextField();
        openDays.setBounds(150, 220, fieldLength, fieldHeight);
    }

    private void setTextFields(){

        String[] fieldsText = {libraryData.getName(), libraryData.getAddress(), libraryData.getPostalCode(), libraryData.getCity(),
                libraryData.getOpenHours(), libraryData.getOpenDays()};

        name.setText(fieldsText[0]);
        address.setText(fieldsText[1]);
        postalCode.setText(fieldsText[2]);
        city.setText(fieldsText[3]);
        openHours.setText(fieldsText[4]);
        openDays.setText(fieldsText[5]);
    }

    private void addFields(){

        add(name);
        add(address);
        add(postalCode);
        add(city);
        add(openHours);
        add(openDays);

    }

    private void setPostalCodeKL() {
        postalCode.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }
            @Override
            public void keyPressed(KeyEvent e) { }
            @Override
            public void keyReleased(KeyEvent e) { city.setText(cityDBService.getCityName(postalCode.getText())); }
        });
    }

    private void createButtons(){

        edit = new MyButton(true);
        edit.setText("Zmień dane");
        edit.setBounds(400,20,200,30);
        add(edit);

        confirm = new MyButton(true);
        confirm.setText("Zatwierdź zmiany");
        confirm.setBounds(400,20,200,30);
        confirm.setVisible(false);
        add(confirm);

        cancel = new MyButton(false);
        cancel.setText("Anuluj");
        cancel.setBounds(400,60,200,30);
        cancel.setVisible(false);
        add(cancel);

    }

    private void actions(){
        edit.addActionListener(e -> {
            confirm.setVisible(true);
            cancel.setVisible(true);
            edit.setVisible(false);
            setEditable(true);
        });

        cancel.addActionListener(e -> {
            setTextFields();
            confirm.setVisible(false);
            cancel.setVisible(false);
            edit.setVisible(true);
            setEditable(false);
        });

        confirm.addActionListener(e -> {
            if (Validation.checkIfPostalCodeOK(city.getText()) == false){
                        JOptionPane.showMessageDialog(this, "Niepoprawny kod pocztowy");
                } else if(check()){
                libraryDataService.updateLibraryDatas(name.getText(), address.getText(), postalCode.getText(), city.getText(), openDays.getText(), openHours.getText());
                confirm.setVisible(false);
                cancel.setVisible(false);
                edit.setVisible(true);
                setEditable(false);
                JOptionPane.showMessageDialog(this, "Dane zostały poprawnie zmienione.");
            } else
                JOptionPane.showMessageDialog(this, "Uzupełnij dane.");
        });
    }

    private void setEditable(boolean editable){
        name.setEditable(editable);
        address.setEditable(editable);
        postalCode.setEditable(editable);
        openHours.setEditable(editable);
        openDays.setEditable(editable);
    }

    private boolean check(){
        return !name.getText().isEmpty() && !address.getText().isEmpty() && !postalCode.getText().isEmpty() && !city.getText().isEmpty()
                && !openHours.getText().isEmpty() && !openDays.getText().isEmpty();
    }
}

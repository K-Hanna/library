package gui.libraryinf;

import city.CityDBServiceImpl;
import city.ICityDBService;
import config.Validation;
import gui.event.ImageFilter;
import gui.general.MyButton;
import images.IPosterDBService;
import images.Poster;
import images.PosterDBServiceImpl;
import libraryData.ILibraryData;
import libraryData.LibraryData;
import libraryData.LibraryDataService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class LibraryDataPanel extends JPanel {

    private JTextField[] fields;
    private MyButton edit, confirm, cancel, browsePoster;
    private JTextArea info;
    private JLabel poster;
    private JTextField posterPath;
    private JFileChooser fileChooser;

    private ILibraryData libraryDataService = new LibraryDataService();
    private ICityDBService cityDBService = new CityDBServiceImpl();
    private IPosterDBService posterDBService = new PosterDBServiceImpl();

    private LibraryData libraryData;

    public LibraryDataPanel(){

        libraryData = libraryDataService.getLibraryDatas();

        setLayout(null);

        createLabels();
        createFields();
        setTextFields();
        createButtons();
        setEditable(false);
        setPostalCodeKL();
        actions();
    }

    private void createLabels() {

        JLabel[] labels = new JLabel[8];
        String[] labelNames = {"Nazwa:", "Adres:", "Kod pocztowy:", "Miasto:", "Godziny otwarcia:", "Dni otwarcia:", "E-mail:", "Telefon:"};

        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel();
            labels[i].setBounds(50, 20 + 40 * i, 100,30);
            labels[i].setText(labelNames[i]);
            add(labels[i]);
        }

        JLabel infoLbl = new JLabel("Informacje:");
        infoLbl.setBounds(50, 20 + 40 * labels.length,100,30);
        add(infoLbl);

        poster = new JLabel();
        poster.setBorder(BorderFactory.createLineBorder(Color.black));
        poster.setBounds(400, 20, 200, 200);
        Poster posterId = posterDBService.readImageById(libraryData.getImage());
        ImageIcon icon = new ImageIcon(posterId.getImgBytes());
        poster.setIcon(icon);
        add(poster);
    }

    private void createFields() {

        fields = new JTextField[8];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField();
            fields[i].setBounds(150, 20 + 40 * i, 200, 30);
            add(fields[i]);
        }

        info = new JTextArea();
        info.setBounds(150,20 + 40 * fields.length,200,60);
        info.setLineWrap(true);
        info.setAlignmentY(TOP_ALIGNMENT);
        info.setWrapStyleWord(true);
        info.setBorder(BorderFactory.createLineBorder(Color.black));
        add(info);

        posterPath = new JTextField();
        posterPath.setBounds(400, 270, 200, 30);
        posterPath.setEditable(false);
        posterPath.setVisible(false);
        add(posterPath);
    }

    private void setTextFields(){

        String[] fieldsText = {libraryData.getName(), libraryData.getAddress(), libraryData.getPostalCode(), libraryData.getCity(),
                libraryData.getOpenHours(), libraryData.getOpenDays(), libraryData.geteMail(), String.valueOf(libraryData.getTelephone())};

        for (int i = 0; i < fields.length; i++) {
            fields[i].setText(fieldsText[i]);
        }

        info.setText(libraryData.getInfo());
    }

    private void setPostalCodeKL() {
        fields[2].addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }
            @Override
            public void keyPressed(KeyEvent e) { }
            @Override
            public void keyReleased(KeyEvent e) { fields[3].setText(cityDBService.getCityName(fields[2].getText())); }
        });
    }

    private void createButtons(){

        edit = new MyButton(true);
        edit.setText("Zmień dane");
        edit.setBounds(400,370,200,30);
        add(edit);

        confirm = new MyButton(true);
        confirm.setText("Zatwierdź zmiany");
        confirm.setBounds(400,330,200,30);
        confirm.setVisible(false);
        add(confirm);

        cancel = new MyButton(false);
        cancel.setText("Anuluj");
        cancel.setBounds(400,370,200,30);
        cancel.setVisible(false);
        add(cancel);

        browsePoster = new MyButton(true);
        browsePoster.setText("Dodaj obrazek");
        browsePoster.setBounds(400,230,200,30);
        browsePoster.setVisible(false);
        add(browsePoster);

    }

    private void actions(){
        edit.addActionListener(e -> {
            confirm.setVisible(true);
            cancel.setVisible(true);
            browsePoster.setVisible(true);
            edit.setVisible(false);
            setEditable(true);
        });

        cancel.addActionListener(e -> {
            setTextFields();
            confirm.setVisible(false);
            cancel.setVisible(false);
            browsePoster.setVisible(false);
            edit.setVisible(true);
            setEditable(false);
        });

        confirm.addActionListener(e -> {
            if (!Validation.checkIfPostalCodeOK(fields[3].getText())) {
                JOptionPane.showMessageDialog(this, "Niepoprawny kod pocztowy.");
            } else if(!Validation.checkIfEmailOK(fields[6].getText())) {
                JOptionPane.showMessageDialog(this, "Niepoprawny email.");
            } else if(!Validation.checkIfPhoneOK(fields[7].getText())){
                JOptionPane.showMessageDialog(this, "Niepoprawny numer telefonu (9 cyfr).");
            } else if(check()){
                libraryDataService.updateLibraryDatas(fields[0].getText(), fields[1].getText(), fields[2].getText(),
                    fields[3].getText(), fields[4].getText(), fields[5].getText(), fields[6].getText(), Integer.parseInt(fields[7].getText()),
                    info.getText());

                if(!posterPath.getText().isEmpty()){
                    posterDBService.addImage(getPosterPath().getText());
                    Poster posterForNewEvent = posterDBService.readLastImageFromDB();
                    int newPosterId = posterForNewEvent.getIdImg();
                    libraryDataService.setLibraryImage(newPosterId);
                }

                confirm.setVisible(false);
                cancel.setVisible(false);
                browsePoster.setVisible(false);
                edit.setVisible(true);
                setEditable(false);

                JOptionPane.showMessageDialog(this, "Dane zostały poprawnie zmienione.");

            } else
                JOptionPane.showMessageDialog(this, "Uzupełnij dane.");
        });

        browsePoster.addActionListener(e -> {
            fileChooser = new JFileChooser("C:\\Users\\e495405\\Desktop\\Baza danych zdjęcia\\biblio\\postery\\200_300");
            fileChooser.addChoosableFileFilter(new ImageFilter());
            fileChooser.setAcceptAllFileFilterUsed(false);
            int r = fileChooser.showOpenDialog(this);
            if(r == JFileChooser.APPROVE_OPTION){
                getPosterPath().setText(fileChooser.getSelectedFile().getAbsolutePath());
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    poster.setIcon(new ImageIcon(ImageIO.read(file)));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else
                getPosterPath().setText("");
        });
    }

    private void setEditable(boolean editable){
        for (JTextField field : fields) {
            field.setEditable(editable);
        }

        info.setEditable(editable);
        if(editable)
            info.setBackground(Color.white);
        else
            info.setBackground(null);
    }

    private boolean check(){

        boolean notEmptyField = true;
        for (JTextField field : fields) {
            if (field.getText().isEmpty())
                notEmptyField = false;
        }

        return notEmptyField;
    }

    private JTextField getPosterPath(){
        return posterPath;
    }
}

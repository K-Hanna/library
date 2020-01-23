package gui.libraryinf;

import images.IPosterDBService;
import images.Poster;
import images.PosterDBServiceImpl;
import libraryData.ILibraryData;
import libraryData.LibraryData;
import libraryData.LibraryDataService;

import javax.swing.*;
import java.awt.*;

public class LibraryDataShowPanel extends JPanel {

    private LibraryData libraryData;

    private IPosterDBService posterDBService = new PosterDBServiceImpl();

    public LibraryDataShowPanel(){

        ILibraryData libraryDataService = new LibraryDataService();
        libraryData = libraryDataService.getLibraryDatas();

        setLayout(null);

        createLabels();
        createFields();
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

        JLabel poster = new JLabel();
        poster.setBorder(BorderFactory.createLineBorder(Color.black));
        poster.setBounds(400, 20, 200, 200);
        Poster posterId = posterDBService.readImageById(libraryData.getImage());
        ImageIcon icon = new ImageIcon(posterId.getImgBytes());
        poster.setIcon(icon);
        add(poster);
    }

    private void createFields() {

        String[] fieldsText = {libraryData.getName(), libraryData.getAddress(), libraryData.getPostalCode(), libraryData.getCity(),
                libraryData.getOpenHours(), libraryData.getOpenDays(), libraryData.geteMail(), String.valueOf(libraryData.getTelephone())};

        JTextField[] fields = new JTextField[8];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField();
            fields[i].setBounds(150, 20 + 40 * i, 200, 30);
            fields[i].setText(fieldsText[i]);
            fields[i].setEditable(false);
            fields[i].setBorder(null);
            add(fields[i]);
        }

        JTextArea info = new JTextArea();
        info.setBounds(150,20 +40 * fields.length,200,60);
        info.setLineWrap(true);
        info.setAlignmentY(TOP_ALIGNMENT);
        info.setWrapStyleWord(true);
        info.setBackground(null);
        info.setText(libraryData.getInfo());
        info.setEditable(false);
        add(info);
    }
}

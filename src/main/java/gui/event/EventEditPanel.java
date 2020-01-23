package gui.event;

import config.Validation;
import event.Event;
import event.EventDBServiceImpl;
import event.IEventDBService;
import gui.general.MyButton;
import images.IPosterDBService;
import images.Poster;
import images.PosterDBServiceImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class EventEditPanel extends JPanel {
    private JLabel titleLbl, dateLbl, shortDescLbl, posterShowLbl;
    private JTextField titleTxt, dateTxt, posterTxt;
    private JTextArea shortDescTxt;
    private MyButton editEventBtn, browsePosterBtn, cancel;
    private int fieldLength = 200;
    private JFileChooser fileChooser;
    private int eventToEdit;
    private Event event;

    private IPosterDBService posterDBService = new PosterDBServiceImpl();
    private IEventDBService eventDBService = new EventDBServiceImpl();

    public EventEditPanel(EventGetPanel eventGetPanel){

        this.eventToEdit = eventGetPanel.getEventIdToEdit();
        event = eventDBService.readEvent(eventToEdit);

        setLayout(null);
        createAllLabels();
        addAllLabels();
        createAllButtons();
        addAllButtons();
        addActionBrowsePosterBtn();
        actionAddEventBtn();
    }

    private void actionAddEventBtn() {
        editEventBtn.addActionListener(e -> {
            if (titleTxt.getText().equals("") || dateTxt.getText().equals("")  || shortDescTxt.getText().equals(""))
                JOptionPane.showMessageDialog(this, "Proszę wypełnić wszystkie pola");
            else if(!Validation.checkIfDateOk(dateTxt.getText()))
                JOptionPane.showMessageDialog(this, "Niepoprawna data");
            else {
                int newPosterId;

                if(getPosterTxt().getText().isEmpty())
                    newPosterId = event.getImgId();
                else {
                    posterDBService.addImage(getPosterTxt().getText());
                    Poster posterForNewEvent = posterDBService.readLastImageFromDB();
                    newPosterId = posterForNewEvent.getIdImg();
                }

                eventDBService.updateEventInDB(eventToEdit, titleTxt.getText(), LocalDate.parse(dateTxt.getText()), newPosterId, shortDescTxt.getText());
                JOptionPane.showMessageDialog(this, "Wydarzenie zedytowane");
                setComponentsEditability(false);
            }
        });
    }

    private void addActionBrowsePosterBtn(){
        browsePosterBtn.addActionListener(e -> {
            fileChooser = new JFileChooser("C:\\Users\\e495405\\Desktop\\Baza danych zdjęcia\\biblio\\postery\\200_300");
            fileChooser.addChoosableFileFilter(new ImageFilter());
            fileChooser.setAcceptAllFileFilterUsed(false);
            int r = fileChooser.showOpenDialog(this);
            if(r == JFileChooser.APPROVE_OPTION) {
                getPosterTxt().setText(fileChooser.getSelectedFile().getAbsolutePath());
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    posterShowLbl.setIcon(new ImageIcon(ImageIO.read(file)));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else
                getPosterTxt().setText("");
        });
    }

    private void addAllButtons() {
        add(editEventBtn);
        add(browsePosterBtn);
        add(cancel);
    }

    private void createAllButtons(){
        createAddBtn();
        createBrowseBtn();
        createDeleteBtn();
    }

    private void createBrowseBtn() {
        browsePosterBtn = new MyButton(true);
        browsePosterBtn.setText("Wyszukaj plakat");
        browsePosterBtn.setBounds(400, 330, 200, 30);
    }

    private void createDeleteBtn() {
        cancel = new MyButton(false);
        cancel.setText("Anuluj");
        cancel.setBounds(150, 370, 200, 30);
    }

    private void createAddBtn() {
        editEventBtn = new MyButton(true);
        editEventBtn.setText("Wprowadź dane");
        editEventBtn.setBounds(150, 330, 200, 30);
    }

    private void addAllLabels() {
        add(titleLbl);
        add(titleTxt);
        add(dateLbl);
        add(dateTxt);
        add(posterTxt);
        add(posterShowLbl);
        add(shortDescLbl);
        add(shortDescTxt);
    }

    private void createAllLabels() {
        createTitleLbl();
        createTitleTxt();
        createDateLbl();
        createDateTxt();
        createPosterShowLbl();
        createPosterTxt();
        createShortDescLbl();
        createShortDescTxt();
    }

    private void createShortDescLbl() {
        shortDescLbl = new JLabel();
        shortDescLbl.setText("Krótki opis");
        shortDescLbl.setBounds(50, 100, 100, 30);
    }

    private void createShortDescTxt() {
        shortDescTxt = new JTextArea();
        shortDescTxt.setText(event.getShortDescription());
        shortDescTxt.setBounds(150, 100, fieldLength, 100);
        shortDescTxt.setBorder(BorderFactory.createLineBorder(Color.black));
        shortDescTxt.setWrapStyleWord(true);
        shortDescTxt.setLineWrap(true);
    }

    private void createPosterShowLbl() {
        posterShowLbl = new JLabel();
        posterShowLbl.setBounds(400, 20, fieldLength, 300);
        posterShowLbl.setBorder(BorderFactory.createLineBorder(Color.black));
        Poster poster = posterDBService.readImageById(event.getImgId());
        ImageIcon icon = new ImageIcon(poster.getImgBytes());
        posterShowLbl.setIcon(icon);
    }

    private void createPosterTxt() {
        posterTxt = new JTextField();
        posterTxt.setBounds(400, 370, fieldLength, 30);
        posterTxt.setEditable(false);
    }

    private void createDateLbl() {
        dateLbl = new JLabel();
        dateLbl.setText("Data");
        dateLbl.setBounds(50, 60, 100, 30);
    }

    private void createDateTxt() {
        dateTxt = new JTextField();
        dateTxt.setText(event.getDateEvent().toString());
        dateTxt.setBounds(150, 60, fieldLength, 30);
    }

    private void createTitleLbl() {
        titleLbl = new JLabel();
        titleLbl.setText("Tytuł");
        titleLbl.setBounds(50, 20, 100, 30);
    }

    private void createTitleTxt() {
        titleTxt = new JTextField();
        titleTxt.setText(event.getTitle());
        titleTxt.setBounds(150, 20, fieldLength, 30);
    }

    private void setComponentsEditability(boolean editability) {
        titleTxt.setEditable(editability);
        dateTxt.setEditable(editability);
        posterTxt.setEditable(editability);
        shortDescTxt.setEditable(editability);
    }

    public JTextField getPosterTxt() {
        return posterTxt;
    }

    public MyButton getCancel() {
        return cancel;
    }
}

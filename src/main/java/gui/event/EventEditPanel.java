package gui.event;

import config.Validation;
import event.Event;
import event.EventDBServiceImpl;
import event.IEventDBService;
import gui.general.MyButton;
import images.IPosterDBService;
import images.Poster;
import images.PosterDBServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class EventEditPanel extends JPanel {
    private JLabel titleLbl, dateLbl, posterLbl, shortDescLbl, posterShowLbl;
    private JTextField titleTxt, dateTxt, posterTxt;
    private JTextArea shortDescTxt;
    private MyButton editEventBtn, browsePosterBtn, cancel;
    private int fieldLength = 200;
    private JFileChooser fileChooser;
    private int eventToEdit;
    int deltaY = 20;
    private Event event;

    private IPosterDBService posterDBService = new PosterDBServiceImpl();
    private IEventDBService eventDBService = new EventDBServiceImpl();

    public EventEditPanel(EventGetPanel eventGetPanel){

        this.eventToEdit = eventGetPanel.getEventIdToEdit();
        event = eventDBService.readEvent(eventToEdit);

        setLayout(null);
        createAllLabels();
        addAllLabels();
        setCompVisibility(true);
        createAllButtons();
        addAllButtons();
        addActionBrowsePosterBtn();
        actionAddEventBtn();
    }

    private void actionAddEventBtn() {
        editEventBtn.addActionListener(e -> {
            if (titleTxt.getText().equals("") || dateTxt.getText().equals("")  || shortDescTxt.getText().equals(""))
                JOptionPane.showMessageDialog(this, "Proszę wypełnić wszystkie pola");
            else if(Validation.checkIfDateOk(dateTxt.getText())== false)
                JOptionPane.showMessageDialog(this, "Niepoprawna data");
            else {
                posterDBService.addImage(getPosterTxt().getText());
                Poster posterForNewEvent = posterDBService.readLastImageFromDB();
                int newPosterId = posterForNewEvent.getIdImg();

                eventDBService.updateEventInDB(eventToEdit, titleTxt.getText(), LocalDate.parse(dateTxt.getText()), newPosterId, shortDescTxt.getText());
                JOptionPane.showMessageDialog(this, "Wydarzenie zedytowane");
                setComponentsEditability(false);
            }
        });

    }

    private void addActionBrowsePosterBtn(){
        browsePosterBtn.addActionListener(e -> {
            fileChooser = new JFileChooser("C:\\Users\\e495405\\Desktop\\Baza danych zdjęcia\\biblio\\postery\\200_300");
            int r = fileChooser.showOpenDialog(this);
            if(r == JFileChooser.APPROVE_OPTION)
                getPosterTxt().setText(fileChooser.getSelectedFile().getAbsolutePath());
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
        browsePosterBtn.setBounds(150, 100, 200, 30);
    }

    private void createDeleteBtn() {
        cancel = new MyButton(false);
        cancel.setText("Anuluj");
        cancel.setBounds(400, 60, 200, 30);
    }

    private void createAddBtn() {
        editEventBtn = new MyButton(true);
        editEventBtn.setText("Wprowadź dane");
        editEventBtn.setBounds(400, 20, 200, 30);
    }

    private void addAllLabels() {
        add(titleLbl);
        add(titleTxt);
        add(dateLbl);
        add(dateTxt);
        add(posterLbl);
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
        createPosterLbl();
        createPosterShowLbl();
        createPosterTxt();
        createShortDescLbl();
        createShortDescTxt();
    }

    private void createShortDescLbl() {
        shortDescLbl = new JLabel();
        shortDescLbl.setText("Krótki opis");
        shortDescLbl.setBounds(50, 180, 100, 30);
    }

    private void createShortDescTxt() {
        shortDescTxt = new JTextArea();
        shortDescTxt.setText(event.getShortDescription());
        shortDescTxt.setBounds(150, 180, fieldLength, 100);
        shortDescTxt.setBorder(BorderFactory.createLineBorder(Color.black));
        shortDescTxt.setWrapStyleWord(true);
        shortDescTxt.setLineWrap(true);
    }


    private void createPosterLbl() {
        posterLbl = new JLabel();
        posterLbl.setText("Plakat");
        posterLbl.setBounds(50, 100, 100, 30);
    }

    private void createPosterShowLbl() {
        posterShowLbl = new JLabel();
        posterShowLbl.setBounds(400, 100, fieldLength, 300);
        Poster poster = posterDBService.readImageById(event.getImgId());
        ImageIcon icon = new ImageIcon(poster.getImgBytes());
        posterShowLbl.setIcon(icon);
    }

    private void createPosterTxt() {
        posterTxt = new JTextField();
        posterTxt.setBounds(150, 140, fieldLength, 30);
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


    private void setCompVisibility(boolean visibility) {
        titleLbl.setVisible(visibility);
        dateLbl.setVisible(visibility);
        posterLbl.setVisible(visibility);
        shortDescLbl.setVisible(visibility);
        titleTxt.setVisible(visibility);
        dateTxt.setVisible(visibility);
        posterTxt.setVisible(visibility);
        shortDescTxt.setVisible(visibility);
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

    public void setPosterTxt(JTextField posterTxt) {
        this.posterTxt = posterTxt;
    }

    public MyButton getCancel() {
        return cancel;
    }
}

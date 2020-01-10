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

public class EventSeeDetailPanel extends JPanel {
    private JLabel titleLbl, dateLbl, shortDescLbl, posterShowLbl;
    private JTextField titleTxt, dateTxt;
    private JTextArea shortDescTxt;
    private MyButton cancel;
    private int fieldLength = 200;
    private int eventToSee;
    private Event event;

    private IPosterDBService posterDBService = new PosterDBServiceImpl();
    private IEventDBService eventDBService = new EventDBServiceImpl();

    public EventSeeDetailPanel(EventShowPanel eventShowPanel){

        this.eventToSee = eventShowPanel.getEventToSee();
        event = eventDBService.readEvent(eventToSee);

        setLayout(null);
        createAllLabels();
        addAllLabels();
        createDeleteBtn();
    }

    private void createDeleteBtn() {
        cancel = new MyButton(false);
        cancel.setText("Powrót");
        cancel.setBounds(400, 350, 200, 30);
        add(cancel);
    }

    private void addAllLabels() {
        add(titleLbl);
        add(titleTxt);
        add(dateLbl);
        add(dateTxt);
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
        shortDescTxt.setEditable(false);
        shortDescTxt.setBounds(150, 100, fieldLength, 100);
        shortDescTxt.setBackground(null);
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

    private void createDateLbl() {
        dateLbl = new JLabel();
        dateLbl.setText("Data");
        dateLbl.setBounds(50, 60, 100, 30);
    }

    private void createDateTxt() {
        dateTxt = new JTextField();
        dateTxt.setText(event.getDateEvent().toString());
        dateTxt.setBounds(150, 60, fieldLength, 30);
        dateTxt.setEditable(false);
        dateTxt.setBorder(null);
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
        titleTxt.setEditable(false);
        titleTxt.setBorder(null);
    }

    public MyButton getCancel() {
        return cancel;
    }
}

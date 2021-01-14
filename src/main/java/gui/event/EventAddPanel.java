package gui.event;

import event.Event;
import event.EventDBServiceImpl;
import event.IEventDBService;
import gui.general.DateLabelFormatter;
import gui.general.MyButton;
import images.IPosterDBService;
import images.Poster;
import images.PosterDBServiceImpl;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;

public class EventAddPanel extends JPanel {
    private JLabel titleLbl, dateLbl,shortDescLbl, posterShowLbl;
    private JTextField titleTxt, posterTxt;
    private JTextArea shortDescTxt;
    private MyButton addEventBtn, browsePosterBtn, cancel, reset;
    private int fieldLength = 200;
    private JFileChooser fileChooser;
    private JDatePickerImpl datePicker;

    private IPosterDBService posterDBService = new PosterDBServiceImpl();
    private IEventDBService eventDBService = new EventDBServiceImpl();

    public EventAddPanel(){

        setLayout(null);
        createAllLabels();
        addAllLabels();
        createAllButtons();
        addAllButtons();
        addActionBrowsePosterBtn();
        createDatePicker();
        actionAddEventBtn();
        actionReset();
    }

    private void createDatePicker(){
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model,p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setBounds(150, 60, fieldLength,30);
        add(datePicker);
    }

    private void actionAddEventBtn() {
        addEventBtn.addActionListener(e -> {
            if (titleTxt.getText().equals("") || datePicker.getJFormattedTextField().getText().equals("") || posterTxt.getText().equals("") || shortDescTxt.getText().equals(""))
                JOptionPane.showMessageDialog(this, "Proszę wypełnić wszystkie pola.");
            else {
                posterDBService.addImage(getPosterTxt().getText());
                Poster posterForNewEvent = posterDBService.readLastImageFromDB();
                int newPosterId = posterForNewEvent.getIdImg();

                Event event = new Event();
                event.setImgId(newPosterId);
                event.setShortDescription(shortDescTxt.getText());
                event.setTitle(titleTxt.getText());
                event.setDateEvent(LocalDate.parse(datePicker.getJFormattedTextField().getText()));
                eventDBService.addEvent(event);
                JOptionPane.showMessageDialog(this, "Nowe wydarzenie zostało dodane do bazy");
            }
        });

    }

    private void addActionBrowsePosterBtn(){
        browsePosterBtn.addActionListener(e -> {
            fileChooser = new JFileChooser("C:\\Users\\e495405\\Desktop\\Baza danych zdjęcia\\biblio\\postery\\200_300");
            fileChooser.addChoosableFileFilter(new ImageFilter());
            fileChooser.setAcceptAllFileFilterUsed(false);
            int r = fileChooser.showOpenDialog(this);
            if(r == JFileChooser.APPROVE_OPTION){
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

    private void actionReset(){
        reset.addActionListener(e -> {
            resetFields();
        });
    }

    private void addAllButtons() {
        add(addEventBtn);
        add(browsePosterBtn);
        add(cancel);
        add(reset);
    }

    private void createAllButtons(){
        createAddBtn();
        createResetBtn();
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

    private void createResetBtn() {
        reset = new MyButton(true);
        reset.setText("Wyczyść");
        reset.setBounds(150, 330, 200, 30);
    }

    private void createAddBtn() {
        addEventBtn = new MyButton(true);
        addEventBtn.setText("Wprowadź dane");
        addEventBtn.setBounds(150, 290, 200, 30);
    }

    private void addAllLabels() {
        add(titleLbl);
        add(titleTxt);
        add(dateLbl);
        add(posterTxt);
        add(posterShowLbl);
        add(shortDescLbl);
        add(shortDescTxt);
    }

    private void createAllLabels() {
        createTitleLbl();
        createTitleTxt();
        createDateLbl();
        createPosterTxt();
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
        shortDescTxt.setBounds(150, 100, fieldLength, 100);
        shortDescTxt.setBorder(BorderFactory.createLineBorder(Color.black));
        shortDescTxt.setWrapStyleWord(true);
        shortDescTxt.setLineWrap(true);
    }

    private void createPosterTxt() {
        posterTxt = new JTextField();
        posterTxt.setBounds(400, 370, fieldLength, 30);
        posterTxt.setVisible(false);
    }

    private void createPosterShowLbl() {
        posterShowLbl = new JLabel();
        posterShowLbl.setBorder(BorderFactory.createLineBorder(Color.black));
        posterShowLbl.setBounds(400, 20, fieldLength, 300);
    }

    private void createDateLbl() {
        dateLbl = new JLabel();
        dateLbl.setText("Data");
        dateLbl.setBounds(50, 60, 100, 30);
    }

    private void createTitleLbl() {
        titleLbl = new JLabel();
        titleLbl.setText("Tytuł");
        titleLbl.setBounds(50, 20, 100, 30);
    }

    private void createTitleTxt() {
        titleTxt = new JTextField();
        titleTxt.setBounds(150, 20, fieldLength, 30);
    }

    private void resetFields() {
        titleTxt.setText("");
        datePicker.getJFormattedTextField().setText("");
        posterTxt.setText("");
        shortDescTxt.setText("");
        posterShowLbl.setIcon(null);
    }

    public JTextField getPosterTxt() {
        return posterTxt;
    }

    public MyButton getCancel() {
        return cancel;
    }
}

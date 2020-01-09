package gui.event;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

import book.*;
import event.Event;
import event.EventDBServiceImpl;
import event.IEventDBService;
import gui.general.MyButton;
import gui.reader.ReaderTabbedPanel;
import reader.IReaderDBService;
import reader.Reader;
import reader.ReaderDBServiceImpl;
import user.User;

public class EventShowPanel extends JPanel {

    private JLabel keyWordLabel, eventsLabel, signedInLabel;
    private JComboBox sortBy1, sortBy2;
    private JList<Event> eventsList, signedInEvents;
    private MyButton search, signIn, resign, seeDetail;
    private int idUser, idReader;
    private Reader reader;
    private JScrollPane scrollPane1, scrollPane2;

    private IEventDBService eventDBService = new EventDBServiceImpl();
    private IReaderDBService readerDBService = new ReaderDBServiceImpl();

    public EventShowPanel(ReaderTabbedPanel readerTabbedPanel) {

        this.idUser = readerTabbedPanel.getIdUser();

        reader = readerDBService.readReaderFromDB(idUser);
        idReader = reader.getIdReader();

        setLayout(null);

        createComps();

        List<Event> readersEvent = eventDBService.getAllEventsForUser(idReader);

        if(readersEvent.size() > 0) {
            createSignedInEvents(readersEvent);
            add(scrollPane2);
        } else {
            signedInLabel.setText("Brak zapisanych wydarzeń.");
        }

        addComps();
        actions();
    }

    private void createEventJList(List<Event> eventList){

        DefaultListModel<Event> listModel = new DefaultListModel<>();
        for (Event event : eventList) {
            listModel.addElement(event);
        }
        eventsList.setModel(listModel);
        eventsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void createSignedInEvents(List<Event> eventList){

        DefaultListModel<Event> listModel = new DefaultListModel<>();
        for (Event event : eventList) {
            listModel.addElement(event);
        }
        signedInEvents.setModel(listModel);
        signedInEvents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void createComps() {

        keyWordLabel = new JLabel("Sortuj po");
        keyWordLabel.setBounds(50,20,100,30);

        sortBy1 = new JComboBox(new String[]{"po nazwie", "po dacie"});
        sortBy1.setBounds(150,20,100,30);

        sortBy2 = new JComboBox(new String[]{"rosnąco", "malejąco"});
        sortBy2.setBounds(250,20,100,30);

        eventsList = new JList();
        eventsList.setBounds(50,60,550,100);

        scrollPane1 = new JScrollPane(eventsList);
        scrollPane1.setBounds(50,60,550,100);
        scrollPane1.setBorder(new TitledBorder("Wyniki wyszukiwania:"));
        scrollPane1.setBackground(Color.white);

        eventsLabel = new JLabel();
        eventsLabel.setBounds(50,60,550,100);
        eventsLabel.setBorder(new TitledBorder("Wyniki wyszukiwania:"));
        eventsLabel.setBackground(Color.white);
        eventsLabel.setOpaque(true);
        eventsLabel.setVerticalAlignment(1);

        signedInEvents = new JList<>();
        signedInEvents.setBounds(50,220,550,100);

        scrollPane2 = new JScrollPane(signedInEvents);
        scrollPane2.setBounds(50,220,550,100);
        scrollPane2.setBorder(new TitledBorder("Zapisane wydarzenia:"));
        scrollPane2.setBackground(Color.white);

        signedInLabel = new JLabel();
        signedInLabel.setBounds(50,220,550,100);
        signedInLabel.setBorder(new TitledBorder("Zapisane wydarzenia:"));
        signedInLabel.setBackground(Color.white);
        signedInLabel.setOpaque(true);
        signedInLabel.setVerticalAlignment(1);

        search = new MyButton(true);
        search.setText("Szukaj");
        search.setBounds(400,20,200,30);

        signIn = new MyButton(true);
        signIn.setText("Zapisz się");
        signIn.setBounds(400,170,200,30);

        resign = new MyButton(true);
        resign.setText("Zrezygnuj");
        resign.setBounds(400,330,200,30);

        seeDetail = new MyButton(true);
        seeDetail.setText("Zobacz szczegóły");
        seeDetail.setBounds(50, 170,200,30);
    }

    private void actions(){

        signIn.addActionListener(e ->{

            Event event = eventsList.getSelectedValue();
            int idEvent = event.getIdEvent();

            if(eventDBService.ifReaderJoined(idEvent,idReader)!=0){
                JOptionPane.showMessageDialog(this,"Już jesteś zapisana/y na to wydarzenie.");
            }
            else {
                eventDBService.joinEvent(idEvent, idReader);

                List<Event> readersEvent = eventDBService.getAllEventsForUser(idReader);
                if(readersEvent.size() > 0) {
                    createSignedInEvents(readersEvent);
                    add(scrollPane2);
                }
                JOptionPane.showMessageDialog(this, "Dołączyłaś/eś do wybranego wydarzenia.");
            }
        });

        search.addActionListener(e -> {

            List<Event>  eventList = eventDBService.getAllEventsFromDB(sortBy1.getSelectedIndex(), sortBy2.getSelectedIndex());
            createEventJList(eventList);
            add(scrollPane1);

        });

        resign.addActionListener(e ->{

            Event event = signedInEvents.getSelectedValue();
            int idEvent = event.getIdEvent();

            if(event == null) {
                JOptionPane.showMessageDialog(this, "Żadne wydarzenie nie zostało wybrane.");
            } else {

                if (JOptionPane.showConfirmDialog(this, "Czy na pewno zrezygnować z wydarzenia?", "UWAGA!",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    eventDBService.resignEvent(idEvent, idReader);
                    List<Event> readersEvent = eventDBService.getAllEventsForUser(idReader);
                    if(readersEvent.size() > 0)
                        createSignedInEvents(readersEvent);
                    else
                        signedInLabel.setText("Brak zapisanych wydarzeń");
                }
            }
        });
    }

    private void addComps(){

        add(keyWordLabel);
        add(sortBy1);
        add(sortBy2);
        add(eventsLabel);
        add(signedInLabel);
        add(search);
        add(signIn);
        add(resign);
        add(seeDetail);
    }

    int getEventToSee(){

        int eventIdToSee = 0;

        Event event = eventsList.getSelectedValue();
        if(event != null) {
            eventIdToSee = event.getIdEvent();
        }

        return eventIdToSee;
    }

    public JList<Event> getEventsList(){
        return eventsList;
    }

    public MyButton getSeeDetail(){
        return seeDetail;
    }

}
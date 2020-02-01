package gui.event;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

import event.Event;
import event.EventDBServiceImpl;
import event.IEventDBService;
import gui.general.MyButton;

public class EventGetPanel extends JPanel {

    private JLabel keyWordLabel, result;
    private JComboBox sortBy1, sortBy2;
    private JList<Event> resultList;
    private MyButton search, remove, edit, create;
    private JScrollPane scrollPane;

    private IEventDBService eventDBService = new EventDBServiceImpl();

    public EventGetPanel() {

        setLayout(null);

        createComps();
        addComps();
        actions();
    }

    private void createEventJList(List<Event> eventList){

        DefaultListModel<Event> listModel = new DefaultListModel<>();
        for (Event event : eventList) {
            listModel.addElement(event);
        }

        resultList.setModel(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void createComps() {

        keyWordLabel = new JLabel("Sortuj po");
        keyWordLabel.setBounds(50,20,100,30);

        sortBy1 = new JComboBox(new String[]{"po nazwie", "po dacie"});
        sortBy1.setBounds(150,20,100,30);

        sortBy2 = new JComboBox(new String[]{"rosnąco", "malejąco"});
        sortBy2.setBounds(250,20,100,30);

        resultList = new JList();
        resultList.setBounds(50,60,300,290);

        scrollPane = new JScrollPane(resultList);
        scrollPane.setBounds(50,60,300,290);
        scrollPane.setBorder(new TitledBorder("Wyniki wyszukiwania:"));
        scrollPane.setBackground(Color.white);

        result = new JLabel();
        result.setBounds(50,60,300,290);
        result.setBorder(new TitledBorder("Wyniki wyszukiwania:"));
        result.setBackground(Color.white);
        result.setOpaque(true);
        result.setVerticalAlignment(1);

        search = new MyButton(true);
        search.setText("Szukaj");
        search.setBounds(400,20,200,30);

        remove = new MyButton(true);
        remove.setText("Usuń");
        remove.setBounds(400,60,200,30);

        edit = new MyButton(true);
        edit.setText("Pokaż");
        edit.setBounds(400,100,200,30);

        create = new MyButton(true);
        create.setText("Dodaj");
        create.setBounds(400,140,200,30);
    }

    private void actions(){

        search.addActionListener(e -> {

            List<Event>  eventList = eventDBService.getAllEventsFromDB(sortBy1.getSelectedIndex(), sortBy2.getSelectedIndex());
            createEventJList(eventList);
            add(scrollPane);

        });

        remove.addActionListener(e ->{
            Event event = resultList.getSelectedValue();

            if(event == null) {
                JOptionPane.showMessageDialog(this, "Żade wydarzenie nie zostało wybrane.");
            } else {

                if (JOptionPane.showConfirmDialog(this, "Czy na pewno usunąć wydarzenie?", "UWAGA!",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    eventDBService.deleteEvent(event.getIdEvent());
                    repaint();
                    revalidate();
                }
            }
        });
    }

    private void addComps(){

        add(keyWordLabel);
        add(sortBy1);
        add(sortBy2);
        add(result);
        add(search);
        add(remove);
        add(edit);
        add(create);
    }

    int getEventIdToEdit(){

        int eventIdToEdit = 0;

        Event event = resultList.getSelectedValue();
        if(event != null) {
            eventIdToEdit = event.getIdEvent();
        }

        return eventIdToEdit;
    }

    public JList<Event> getResultList(){
        return resultList;
    }

    public MyButton getCreate(){
        return create;
    }

    public MyButton getEdit(){
        return edit;
    }

}
package gui.admin;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

import admin.AdminDBServiceImpl;
import admin.IAdminDBService;
import book.*;
import gui.general.MyButton;
import admin.Admin;
import librarian.Librarian;
import reader.IReaderDBService;
import reader.ReaderDBServiceImpl;
import user.IUserDBService;
import user.User;
import user.UserDBServiceImpl;

public class AdminGetPanel extends JPanel {

    private static JList<Admin> resultList;
    private MyButton create, remove, show;
    private JScrollPane scrollPane;

    private IAdminDBService adminDBService = new AdminDBServiceImpl();
    private IUserDBService userDBService = new UserDBServiceImpl();

    public AdminGetPanel() {

        setLayout(null);
        createComps();

        List<Admin> admins = adminDBService.getAllAdminsFromDB();
        createAdminsJList(admins);
        add(scrollPane);

        addComps();
        actions();
    }

    static void createAdminsJList(List<Admin> admins){

        DefaultListModel listModel = new DefaultListModel();
        for (Admin admin : admins) {
            listModel.addElement(admin);
        }

        resultList.setModel(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }

    private void createComps() {

        resultList = new JList();
        resultList.setBounds(50,20,300,290);

        scrollPane = new JScrollPane(resultList);
        scrollPane.setBounds(50,20,300,290);
        scrollPane.setBorder(new TitledBorder("Wyniki wyszukiwania:"));
        scrollPane.setBackground(Color.white);

        create = new MyButton(true);
        create.setText("Dodaj administratora");
        create.setBounds(400,20,200,30);

        show = new MyButton(true);
        show.setText("Pokaż administratora");
        show.setBounds(400,60,200,30);

        remove = new MyButton(true);
        remove.setText("Usuń administratira");
        remove.setBounds(400,100,200,30);

    }

    private void actions(){

        remove.addActionListener(e ->{
            Admin admin = resultList.getSelectedValue();

            if(admin == null) {
                JOptionPane.showMessageDialog(this, "Żaden administrator nie został wybrany.");
            } else {

                if (JOptionPane.showConfirmDialog(this, "Czy na pewno usunąć administratora?", "UWAGA!",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                    adminDBService.deleteAdminFromDB(admin.getUserId());
                    userDBService.deleteUserFromDB(admin.getCardNumber());

                    List<Admin> admins = adminDBService.getAllAdminsFromDB();
                    createAdminsJList(admins);
                }
            }
        });
    }

    private void addComps(){
        add(create);
        add(remove);
        add(show);
    }

    public int getAdminCard(){

        int adminCard = 0;

        User user =resultList.getSelectedValue();
        if(user != null) {
            adminCard = user.getCardNumber();
        }

        return adminCard;
    }

    MyButton getShow(){
        return show;
    }

    MyButton getCreate(){
        return create;
    }

    JList getResultList(){
        return resultList;
    }
}
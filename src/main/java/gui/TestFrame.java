package gui;

import gui.admin.AdminTabbedPanel;
import gui.librarian.LibrarianTabbedPanel;

import javax.swing.*;

public class TestFrame extends JFrame {

    public TestFrame() {

        setSize(700,600);

//        AdminEntryPanel adminEntryPanel = new AdminEntryPanel();
//        add(adminEntryPanel);

        AdminTabbedPanel adminTabbedPanel = new AdminTabbedPanel();
        add(adminTabbedPanel);

//        LibrarianTabbedPanel librarianTabbedPanel = new LibrarianTabbedPanel();
//        add(librarianTabbedPanel);

    }
}

package gui;

import gui.book.BookEditPanel;
import gui.book.BookGetPanel;
import gui.general.BackgroundLabel;
import gui.general.BackgroundPanel;
import gui.librarian.LibrarianTabbedPanel;

import javax.swing.*;

public class TestFrame extends JFrame {

    public TestFrame() {

        setSize(700,600);

//        BackgroundPanel backgroundPanel = new BackgroundPanel();
//        add(backgroundPanel);

        LibrarianTabbedPanel librarianTabbedPanel = new LibrarianTabbedPanel();
        add(librarianTabbedPanel);

    }
}

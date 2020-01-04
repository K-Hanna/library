package gui.general;

import javax.swing.*;

public class BackgroundPanel extends JPanel {

    public BackgroundPanel() {

        BackgroundLabel myLabel = new BackgroundLabel();
        myLabel.setBounds(0,0,700,600);

        add(myLabel);
    }
}
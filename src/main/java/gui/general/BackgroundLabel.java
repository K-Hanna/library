package gui.general;

import javax.swing.*;

public class BackgroundLabel extends JLabel{

    public BackgroundLabel(){

        setBounds(0,0, 700, 600);

        ImageIcon image = null;

        try {
            image = new ImageIcon(this.getClass().getClassLoader().getResource("library.jpg"));
        } catch (Exception e) {
            System.out.println("Problem with picture.");
        }

        setIcon(image);
        setVisible(true);
        setOpaque(true);
    }
}

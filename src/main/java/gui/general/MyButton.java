package gui.general;

import javax.swing.*;
import java.awt.*;

public class MyButton extends JButton {

    public MyButton(boolean option){

        if(option){
            setBackground(new Color(89,53,37));
            setForeground(Color.white);
        } else {
            setForeground(new Color(89,53,37));
            setBorder(null);
            setContentAreaFilled(false);
        }
    }

}

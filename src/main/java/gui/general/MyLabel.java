package gui.general;

import javax.swing.*;
import java.awt.*;

public class MyLabel extends JLabel {

    public MyLabel(){
        setBackground(new Color(255,255,255,150));
        setOpaque(true);
        setBorder(BorderFactory.createDashedBorder(null));
    }
}

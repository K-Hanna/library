
import gui.MFrame;
import gui.TestFrame;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
//            MFrame window = new MFrame();
            TestFrame window = new TestFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setVisible(true);
        });

    }
}

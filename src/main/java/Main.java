
import gui.MFrame;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("Czytelnik: 150035, aa");
        System.out.println("Bibliotekarz: 150047, aa");
        System.out.println("Admin: 150045, aa");

        EventQueue.invokeLater(() -> {
            MFrame window = new MFrame();
//            TestFrame window = new TestFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setVisible(true);
        });

        //try me

    }
}

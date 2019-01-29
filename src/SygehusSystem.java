import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static javax.swing.SwingConstants.*;

@SuppressWarnings("Duplicates")

public class SygehusSystem {

    ArrayList<Bruger> brugerListe = new ArrayList<>();
    SygehusGUI gui = new SygehusGUI();

    class SygehusGUI {
        JFrame frame;
        JPanel panel;

        public SygehusGUI() {
            this.init();
        }

        public void init() {
            frame = new JFrame("HospitalOS");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            panel = new JPanel();

            JButton button1 = new JButton("Login");
            JButton button2 = new JButton("Lav ny bruger");


            panel.add(button1);
            panel.add(button2);



            button1.addActionListener(e -> drawLogin());
            button2.addActionListener(e -> drawSignup());

            frame.getContentPane().add(panel);
            frame.setSize(500,500);
            frame.setVisible(true);
        }

        public void drawLogin() {
            Container contain = frame.getContentPane();
            contain.removeAll();
            JPanel newPanel = new JPanel();
            JLabel label = new JLabel("You went to page 1!");

            newPanel.add(label);
            contain.add(newPanel);

            frame.validate();
            frame.repaint();
            frame.setVisible(true);
        }

        public void drawSignup() {
            Container contain = frame.getContentPane();
            contain.removeAll();
            JPanel newPanel = new JPanel();
            JLabel newLabel = new JLabel("You went to page 2!");
            newPanel.add(newLabel);
            contain.add(newPanel);
            frame.validate();
            frame.repaint();
            frame.setVisible(true);
        }
    }

}

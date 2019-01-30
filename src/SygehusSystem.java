import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            this.init();
        }

        public void init() {
            frame = new JFrame("HospitalOS");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            ImagePanel image = new ImagePanel("hospital.jpg",420,122);

            panel = new JPanel(null);
            panel.setBounds(1000,1000,1000,1000);

            panel.add(image);
            image.setBounds(300,100,420,122);

            JButton button1 = new JButton("Login");
            button1.setBounds(400,300,200,50);
            JButton button2 = new JButton("Lav ny bruger");
            button2.setBounds(400,400,200,50);

            panel.add(button1);
            panel.add(button2);


            button1.addActionListener(e -> drawLogin());
            button2.addActionListener(e -> drawSignup());



            frame.setContentPane(panel);
            frame.setSize(1000,1000);

            frame.setVisible(true);
        }

        public void drawLogin() {
            redrawBasic();
            panel.add(new JLabel("Page 1"));
            refreshFrame();
        }

        public void drawSignup() {
            redrawBasic();
            panel.add(new JLabel("Page 2"));
            refreshFrame();
        }

        public void redrawBasic() {
            frame.getContentPane().removeAll();
            panel = new JPanel();
            frame.setContentPane(panel);

        }

        public void refreshFrame() {
            frame.validate();
            frame.repaint();
            frame.setVisible(true);
        }
    }

    class ImagePanel extends JPanel {
        private BufferedImage image;
        int w, h;
        public ImagePanel(String path, int wIn, int hIn) {
            w = wIn;
            h = hIn;
            try {
                image = ImageIO.read(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Image newImage = image.getScaledInstance(w,h,Image.SCALE_DEFAULT);
            g.drawImage(newImage, 0,0,this);
        }


    }


}

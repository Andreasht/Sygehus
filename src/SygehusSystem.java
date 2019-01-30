import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static andUtils.Utils.*;
import static andUtils.Serializer.*;
import static javax.swing.SwingConstants.*;

@SuppressWarnings("Duplicates")

class SygehusSystem {

    private ArrayList<Bruger> brugerListe;
    private Bruger activeBruger;

    SygehusSystem() {
        File listFile = new File("brugerListe.ser");
        if (!listFile.exists()) {
            System.out.println("Liste ikke fundet. ArrayList bliver initialiseret.");
            brugerListe = new ArrayList<>();

        } else {
            System.out.println("Liste fundet. Loader liste.");
            loadList();
            System.out.println("Liste loadet.");
        }

        new SygehusGUI();
    }

    class SygehusGUI {
        JFrame frame;
        JPanel panel;

        SygehusGUI() {
            try {
                setUIFont("Segoe UI Semilight", Font.PLAIN, 14);
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
                e.printStackTrace();
            }
            this.init();
        }

        void init() {
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

        void drawLogin() {
            redrawBasic();
            panel.setLayout(null);
            panel.setBounds(1000,1000,1000,1000);

            ArrayList<JLabel> labels = new ArrayList<>();
            labels.add(new JLabel("Brugernavn:"));
            labels.add(new JLabel("Kodeord:"));

            JTextField brugerField = new JTextField();
            JPasswordField kodeField = new JPasswordField();

            ArrayList<JTextComponent> fields = new ArrayList<>();
            fields.add(brugerField);
            fields.add(kodeField);

            int y = 200;
            for(JLabel l : labels) {
                l.setBounds(350, y, 150, 150);
                y = y + 100;
                panel.add(l);
            }
            y = 250;
            for(JTextComponent f : fields) {
                f.setBounds(480,y,150,50);
                y = y + 100;
                panel.add(f);
            }

            JButton logInButton = new JButton("Login");
            logInButton.setBounds(400, 550,200,50);
            panel.add(logInButton);

            logInButton.addActionListener(e -> {
                boolean foundUser = false;
                for(Bruger b : brugerListe) {
                    if(b.getBrugernavn().equals(brugerField.getText())) {
                        foundUser = true;
                        System.out.println("Ding");
                        //noinspection PointlessBooleanExpression
                        if(b.authenticate(kodeField.getPassword()) == true) {
                            System.out.println("Dong");
                            new PopUp().infoBox("Du er nu logget ind som: "+b.getBrugernavn());
                            activeBruger = b;
                            drawProfile();
                        } else {
                            new PopUp().infoBox("Forkert kodeord. Prøv igen.");
                        }
                    }
                }
                if(!foundUser) {
                    new PopUp().infoBox("Ingen bruger med dette navn fundet.");
                }
            });

            refreshFrame();
        }

        void drawSignup() {
            redrawBasic();
            panel.setLayout(null);
            panel.setBounds(1000,1000,1000,1000);

            ArrayList<JLabel> labels = new ArrayList<>();
            labels.add(new JLabel("Fulde navn:"));
            labels.add(new JLabel("Brugernavn:"));
            labels.add(new JLabel("Kodeord:"));
            labels.add(new JLabel("Confirm kodeord:"));

            JTextField navnField = new JTextField();
            JTextField brugerField = new JTextField();
            JPasswordField kodeField = new JPasswordField();
            JPasswordField confKodeField = new JPasswordField();

            ArrayList<JTextComponent> fields = new ArrayList<>();
            fields.add(navnField);
            fields.add(brugerField);
            fields.add(kodeField);
            fields.add(confKodeField);

            int y = 200;
            for(JLabel l : labels) {
                l.setBounds(350, y, 150, 150);
                y = y + 100;
                panel.add(l);
            }
            y = 250;
            for(JTextComponent f : fields) {
                f.setBounds(480,y,150,50);
                y = y + 100;
                panel.add(f);
            }

            JButton createUserButton = new JButton("Lav bruger");
            createUserButton.setBounds(400, 650,200,50);
            panel.add(createUserButton);

            createUserButton.addActionListener(e -> {
                String n = navnField.getText();
                String bN = brugerField.getText();
                char[] k = kodeField.getPassword();
                boolean duplicateName = false;
                for (Bruger bruger : brugerListe) {
                    if (bruger.getBrugernavn().equals(bN)) {
                        duplicateName = true;
                    }
                }

                if(!Arrays.equals(k, confKodeField.getPassword())) {
                    new PopUp().infoBox("Du indtastede ikke den samme kode. Prøv igen.");
                } else if (duplicateName) {
                    new PopUp().infoBox("En bruger med dette brugernavn findes allerede.");
                } else {
                    Bruger temp = new Bruger(n, bN, k);
                    brugerListe.add(temp);
                    System.out.println("En ny bruger blev lavet. Info:"+"\n"+n+"\n"+bN+"\n"+ Arrays.toString(k));
                    saveList();
                    System.out.println("Bruger gemt. Index: "+brugerListe.indexOf(temp));
                    new PopUp().infoBox("Du lavede en ny bruger med brugernavnet: "+bN);
                    activeBruger = temp;
                    drawProfile();
                }

            });

            refreshFrame();
        }

        void drawProfile() {
            redrawBasic();
            panel.add(Box.createRigidArea(new Dimension(0,300)));
            JLabel profileLabel = new JLabel("Du er logget ind som: " + activeBruger.getNavn() +" / "+activeBruger.getBrugernavn());

            panel.add(profileLabel);
            refreshFrame();
        }

        void redrawBasic() {
            frame.getContentPane().removeAll();
            panel = new JPanel();
            frame.setContentPane(panel);

        }

        void refreshFrame() {
            frame.validate();
            frame.repaint();
            frame.setVisible(true);
        }



    }

    private void saveList() {
        serialize(brugerListe, "brugerListe.ser");
        System.out.println("Liste blev gemt.");
    }

    private void loadList() {
        try {
            //noinspection unchecked
            brugerListe = (ArrayList<Bruger>) deserialize("brugerListe.ser");
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Der skete en fejl. Stack trace:");
            ex.printStackTrace();
        }

    }



    class ImagePanel extends JPanel {
        private BufferedImage image;
        final int w;
        final int h;
        ImagePanel(String path, int wIn, int hIn) {
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

    class PopUp
    {
        public void infoBox(String infoMessage)
        {
            JOptionPane.showMessageDialog(null, infoMessage, "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

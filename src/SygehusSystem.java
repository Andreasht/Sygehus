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
import java.util.stream.Collectors;

import static andUtils.Utils.*;
import static andUtils.Serializer.*;
import static javax.swing.SwingConstants.*;

@SuppressWarnings("Duplicates")

class SygehusSystem {

    private ArrayList<Bruger> brugerListe;
    private Bruger activeBruger;

    SygehusSystem() {
        try {
            setUIFont("Segoe UI Semilight", Font.PLAIN, 14);
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
        File listFile = new File("brugerListe.ser");
        if (!listFile.exists()) {
            System.out.println("Liste ikke fundet. ArrayList bliver initialiseret.");
            brugerListe = new ArrayList<>();

        } else {
            System.out.println("Liste fundet. Loader liste.");
            loadList();
            System.out.println("Liste loadet.");
        }
        boolean createAdmin = true;
        for(Bruger b : brugerListe) {
            if (b.getBrugernavn().equals("admin")) createAdmin = false;
        }
        if (createAdmin) brugerListe.add(new Admin("Systemadministrator","admin",Admin.DEF_ADMIN_KODE));

        new SygehusGUI();
    }

    class SygehusGUI {
        JFrame frame;
        JPanel panel;
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem turnOff = new JMenuItem("Luk system");
        JMenuItem goBack = new JMenuItem("Gå tilbage");
        JMenuItem goBackSignup = new JMenuItem("Gå tilbage");

        ArrayList<JButton> profileButtons = new ArrayList<>();

        SygehusGUI() {

            this.init();
        }

        void init() {
            frame = new JFrame("HospitalOS");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            turnOff.addActionListener(e -> System.exit(0));
            menu.add(turnOff);
            menuBar.add(menu);
            frame.setJMenuBar(menuBar);

            ImagePanel image = new ImagePanel("sst.png",732,172);

            panel = new JPanel(null);
            panel.setBounds(1000,1000,1000,1000);

            panel.add(image);
            image.setBounds(134,100,732,172);

            JButton button1 = new JButton("Login");
            button1.setBounds(400,350,200,50);

            panel.add(button1);

            button1.addActionListener(e -> drawLogin());

            frame.setContentPane(panel);
            frame.setSize(1000,1000);
            frame.setResizable(false);
            frame.setVisible(true);
        }

        void drawLogin() {
            redrawBasic();
            panel.setLayout(null);
            panel.setBounds(1000,1000,1000,1000);
            menu.remove(goBackSignup);
            menu.remove(goBack);
            turnOff.addActionListener(e -> System.exit(0));
            menu.add(turnOff);
            menuBar.add(menu);
            frame.setJMenuBar(menuBar);

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

            brugerField.requestFocus();

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
            menu.remove(goBack);
            turnOff.addActionListener(e -> System.exit(0));
            goBackSignup.addActionListener(e -> drawProfile());
            menu.add(goBackSignup);
            menu.add(turnOff);
            menuBar.add(menu);
            frame.setJMenuBar(menuBar);

            ArrayList<JLabel> labels = new ArrayList<>();
            labels.add(new JLabel("Fulde navn:"));
            labels.add(new JLabel("Brugernavn:"));
            labels.add(new JLabel("Kodeord:"));
            labels.add(new JLabel("Confirm kodeord:"));
            labels.add(new JLabel("Vælg type bruger:"));

            JTextField navnField = new JTextField();
            JTextField brugerField = new JTextField();
            JPasswordField kodeField = new JPasswordField();
            JPasswordField confKodeField = new JPasswordField();

            ArrayList<JTextComponent> fields = new ArrayList<>();
            fields.add(navnField);
            fields.add(brugerField);
            fields.add(kodeField);
            fields.add(confKodeField);



            int y = 100;
            for(JLabel l : labels) {
                l.setBounds(350, y, 150, 150);
                y = y + 100;
                panel.add(l);
            }

            y = 150;
            for(JTextComponent f : fields) {
                f.setBounds(480,y,150,50);
                y = y + 100;
                panel.add(f);
            }



            //noinspection unchecked
            JComboBox roleList = new JComboBox(Bruger.ROLE_LIST);
            roleList.setBounds(480,550,150,50);
            panel.add(roleList);
            JButton createUserButton = new JButton("Lav bruger");
            createUserButton.setBounds(400, 650,200,50);
            panel.add(createUserButton);

            navnField.requestFocus();

            createUserButton.addActionListener(e -> {
                String n = navnField.getText();
                String bN = brugerField.getText();
                char[] k = kodeField.getPassword();
                String r = (String) roleList.getSelectedItem();
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
                    Bruger temp = null;
                    switch(r) {

                        case "Admin": temp = new Admin(n,bN,k); break;

      //                  case "Læge": temp = new Læge(n, bN, k); break;

      //                  case "Sygeplejerske": temp = new Sygeplejerske(n, bN, k); break;

        //                case "Andet": temp = new AndetPersonale(n,bN,k);

                        default: System.out.println("wtf happened"); break;

                    }

                    brugerListe.add(temp);
                    System.out.println("En ny bruger blev lavet. Info:"+"\n"+n+"\n"+bN+"\n"+ Arrays.toString(k));
                    saveList();
                    System.out.println("Bruger gemt. Index: "+brugerListe.indexOf(temp));
                    new PopUp().infoBox("Du lavede en ny bruger med brugernavnet: "+bN);
                }

            });

            refreshFrame();
        }

        void drawProfile() {
            redrawBasic();
            menu.remove(goBackSignup);
            turnOff.addActionListener(e -> System.exit(0));
            goBack.addActionListener(e -> drawLogin());
            menu.add(goBack);
            menu.add(turnOff);
            menuBar.add(menu);
            frame.setJMenuBar(menuBar);

            panel.add(Box.createRigidArea(new Dimension(300,250)));
            panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
            JLabel profileLabel = new JLabel("Du er logget ind som: " + activeBruger.getNavn() +" / "+activeBruger.getBrugernavn());
            JLabel roleLabel = new JLabel("Du har rollen: " + activeBruger.getRolle());
            JLabel optLabel = new JLabel("Dette giver dig følgende muligheder: ");
            panel.add(profileLabel);
            panel.add(Box.createVerticalStrut(10));
            panel.add(roleLabel);
            panel.add(Box.createVerticalStrut(10));
            panel.add(optLabel);
            switch(activeBruger.getRolle()) {
                case "Admin": drawAdmin();
            }
            refreshFrame();
        }

        void drawAdmin() {
            profileButtons.clear();
            profileButtons.add(new JButton("Opret ny bruger"));
            profileButtons.add(new JButton("Se liste over brugere"));
            profileButtons.add(new JButton("Sæt din status"));
            profileButtons.add(new JButton("Administrer roller"));
            drawProfileButtons();
            profileButtons.get(0).addActionListener(e -> drawSignup());
            profileButtons.get(1).addActionListener(e -> drawUserList());
            profileButtons.get(2).addActionListener(e -> drawStatusBox());
            profileButtons.get(3).addActionListener(e -> new PopUp().infoBox("Placeholder"));
            refreshFrame();
        }

        void drawProfileButtons() {
            profileButtons.forEach(JButton -> {
                panel.add(Box.createVerticalStrut(50));
                panel.add(JButton);
            });
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

        void drawUserList() {
            JFrame listFrame = new JFrame();
            listFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            JPanel listPanel = new JPanel();
            String[] navneArray = brugerListe.stream().map(b -> b.getNavn() + " / " + b.getBrugernavn() + " | Status: " + b.getStatus()).toArray(String[]::new);
            JList<String> navneListe = new JList<>(navneArray);
            JScrollPane listScroller = new JScrollPane(navneListe);
            listScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            listScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            listPanel.add(navneListe);
            listFrame.setContentPane(listPanel);
            listFrame.setSize(500,500);
            listFrame.validate();
            listFrame.repaint();
            listFrame.setVisible(true);
        }

        void drawStatusBox() {
            Object[] options = {"Ledig", "Optaget", "Fraværende", "Pause"};
            int choice = JOptionPane.showOptionDialog(null, "Hvilken status?", "Status", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            activeBruger.setStatus(choice);
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
        } catch (ClassNotFoundException ex) {
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

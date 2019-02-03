import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.peer.PopupMenuPeer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static andUtils.Utils.*;
import static andUtils.Serializer.*;

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
        File listFile = new File("Data/brugerListe.ser");
        if (!listFile.exists()) {
            brugerListe = new ArrayList<>();

        } else {
            loadList();
        }
        Patient.loadPatientList();
        boolean createDef = true;
        for(Bruger b : brugerListe) {
            if (b.getBrugernavn().equals("admin")) createDef = false;
        }
        if (createDef) {
            brugerListe.add(new Admin("Systemadministrator","admin",Admin.DEF_ADMIN_KODE));
            brugerListe.add(new Læge("Læge","læge",Læge.DEF_LÆGE_KODE));
            brugerListe.add(new Sygeplejerske("Sygeplejerske","sygp",Sygeplejerske.DEF_SYGP_KODE));
            brugerListe.add(new Personel("Personel","pers",Personel.DEF_PERS_KODE));
        }
        new SygehusGUI();
    }

    @SuppressWarnings("NonAsciiCharacters")
    class SygehusGUI {
        JFrame frame;
        JFrame listFrame;
        JPanel panel;
        JPanel listPanel;
        JList<String> navneListe;
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

            ImagePanel image = new ImagePanel("Billeder/sst.png",732,172);

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
                        //noinspection PointlessBooleanExpression
                        if(b.authenticate(kodeField.getPassword()) == true) {
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
                    switch(Objects.requireNonNull(r)) {

                        case "Admin": temp = new Admin(n,bN,k); break;

                        case "Læge": temp = new Læge(n, bN, k); break;

                        case "Sygeplejerske": temp = new Sygeplejerske(n, bN, k); break;

                        case "Personel": temp = new Personel(n,bN,k);

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

            panel.add(Box.createRigidArea(new Dimension(300,200)));
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
                case "Admin": drawAdmin(); break;
                case "Læge": drawLæge(); break;
                case "Sygeplejerske": drawSygp(); break;
                case "Personel": drawPersonel(); break;
                default: System.out.println("Der skete en fejl i drawProfile()"); break;
            }
            refreshFrame();
        }

        void drawAdmin() {
            profileButtons.clear();
            profileButtons.add(new JButton("Opret ny bruger"));
            profileButtons.add(new JButton("Se liste over brugere"));
            profileButtons.add(new JButton("Sæt din status"));
            profileButtons.add(new JButton("Åben personlig mappe"));
            profileButtons.get(0).addActionListener(e -> drawSignup());
            profileButtons.get(1).addActionListener(e -> drawUserList());
            profileButtons.get(2).addActionListener(e -> drawStatusBox());
            profileButtons.get(3).addActionListener(e -> activeBruger.åbenMappe());
            drawProfileButtons();

            refreshFrame();
        }

        void drawLæge() {
            profileButtons.clear();
            profileButtons.add(new JButton("Indskriv patient"));
            profileButtons.add(new JButton("Book seng til patient"));
            profileButtons.add(new JButton("Opret patientjournal"));
            profileButtons.add(new JButton("Læs patientjournaler"));
            profileButtons.add(new JButton("Se indskrevne patienter"));
            profileButtons.add(new JButton("Se liste over brugere"));
            profileButtons.add(new JButton("Sæt din status"));
            profileButtons.add(new JButton("Åben personlig mappe"));
            profileButtons.get(0).addActionListener(e -> drawIndskriv());
            profileButtons.get(1).addActionListener(e -> drawBookSeng());
            profileButtons.get(2).addActionListener(e -> drawOpretPart1());
            profileButtons.get(3).addActionListener(e -> drawLæsJournaler());
            profileButtons.get(4).addActionListener(e -> drawIndskrevneList());
            profileButtons.get(5).addActionListener(e -> drawUserList());
            profileButtons.get(6).addActionListener(e -> drawStatusBox());
            profileButtons.get(7).addActionListener(e -> activeBruger.åbenMappe());

            drawProfileButtons();

            refreshFrame();
        }

        void drawSygp() {
            profileButtons.clear();
            profileButtons.add(new JButton("Indskriv patient"));
            profileButtons.add(new JButton("Book seng til patient"));
            profileButtons.add(new JButton("Læs patientjournaler"));
            profileButtons.add(new JButton("Se indskrevne patienter"));
            profileButtons.add(new JButton("Se liste over brugere"));
            profileButtons.add(new JButton("Sæt din status"));
            profileButtons.add(new JButton("Åben personlig mappe"));
            profileButtons.get(0).addActionListener(e -> drawIndskriv());
            profileButtons.get(1).addActionListener(e -> drawBookSeng());
            profileButtons.get(2).addActionListener(e -> drawLæsJournaler());
            profileButtons.get(3).addActionListener(e -> drawIndskrevneList());
            profileButtons.get(4).addActionListener(e -> drawUserList());
            profileButtons.get(5).addActionListener(e -> drawStatusBox());
            profileButtons.get(6).addActionListener(e -> activeBruger.åbenMappe());

            drawProfileButtons();

            refreshFrame();
        }

        void drawPersonel() {
            profileButtons.clear();
            profileButtons.add(new JButton("Se liste over brugere"));
            profileButtons.add(new JButton("Sæt din status"));
            profileButtons.add(new JButton("Åben personlig mappe"));
            profileButtons.get(0).addActionListener(e -> drawUserList());
            profileButtons.get(1).addActionListener(e -> drawStatusBox());
            profileButtons.get(2).addActionListener(e -> activeBruger.åbenMappe());

            drawProfileButtons();

            refreshFrame();
        }

        void drawIndskriv() {
            JFrame indskrivFrame = new JFrame("Indskriv patient");
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));

            JTextArea navn = new JTextArea(1,20);
            navn.setLineWrap(true);
            navn.setWrapStyleWord(true);

            JScrollPane nScroller = new JScrollPane(navn);
            nScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            nScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            JTextArea cpr = new JTextArea(1,1);
            cpr.setLineWrap(true);
            cpr.setWrapStyleWord(true);


            JScrollPane cprScroller = new JScrollPane(cpr);
            cprScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            cprScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            JTextArea grund = new JTextArea(3,20);
            grund.setLineWrap(true);
            grund.setWrapStyleWord(true);

            JScrollPane gScroller = new JScrollPane(grund);
            gScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            gScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            JButton indskrivButton = new JButton("Indskriv");

            JLabel navnLabel = new JLabel("Navn på patient:");
            JLabel cprLabel = new JLabel("CPR-nummer:");
            JLabel grundLabel = new JLabel("Grund til indlæggelse:");
            mainPanel.add(navnLabel);
            mainPanel.add(nScroller);
            mainPanel.add(cprLabel);
            mainPanel.add(cprScroller);
            mainPanel.add(grundLabel);
            mainPanel.add(gScroller);
            mainPanel.add(indskrivButton);

            indskrivButton.addActionListener(e -> {
                long cprLong;
                if(!(cpr.getText().length() == 10)) {
                    new PopUp().infoBox("Ugyldigt CPR-nummer. CPR-nummer skal være på 10 cifre.");
                }
                else {
                    cprLong = Long.parseLong(cpr.getText());
                    Patient.indskriv(new Patient(navn.getText(), cprLong), grund.getText());
                    new PopUp().infoBox("En patient med navnet "+navn.getText()+" blev indskrevet.");
                    Patient.savePatientList();
                    indskrivFrame.dispose();
                }
            });
            navn.requestFocus();
            indskrivFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            indskrivFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);
            indskrivFrame.setSize(500,500);
            indskrivFrame.setVisible(true);
        }

        void drawIndskrevneList() {
            drawListFrame(false, false);
            JButton seDetaljer = new JButton("Se detaljer");
            JButton udskriv = new JButton("Udskriv");
            listPanel.add(seDetaljer);
            listPanel.add(udskriv);
            seDetaljer.addActionListener(e -> {
                String[] temp = navneListe.getSelectedValue().split("\\|");
                long id = Long.parseLong(temp[1].substring(6,16));
                for(Patient p : Patient.patientListe) {
                    if(p.getCpr() == id) {
                        new PopUp().infoBox("Grund til indlæggelse:\n"+p.getGrund());
                    }
                }

            });
            udskriv.addActionListener(ev -> {
                String[] temp = navneListe.getSelectedValue().split("\\|");
                long id = Long.parseLong(temp[1].substring(6,16));
                for(Patient p : Patient.patientListe) {
                    if(p.getCpr() == id) {
                        new PopUp().infoBox("Patienten "+p.getNavn()+" blev udskrevet.");
                        Patient.udskriv(p);
                        Patient.savePatientList();
                        break;
                    }
                }
            });
            refreshListFrame();
        }

        void drawOpretPart1() {
            drawListFrame(false, false);
            JButton chooseButton = new JButton("Vælg");
            listPanel.add(chooseButton);
            chooseButton.addActionListener(e -> {
                String[] temp = navneListe.getSelectedValue().split("\\|");
                long id = Long.parseLong(temp[1].substring(6,16));
                for(Patient p : Patient.patientListe) {
                    if(p.getCpr() == id) {
                        drawOpretPart2(p);
                        listFrame.dispose();
                    }
                }
            });
            refreshListFrame();
        }

        void drawOpretPart2(Patient p) {
            JFrame listFrame = new JFrame();
            listFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            JPanel listPanel = new JPanel();

            JTextArea tekst = new JTextArea(10,20);
            tekst.setLineWrap(true);
            tekst.setWrapStyleWord(true);

            JScrollPane Scroller = new JScrollPane(tekst);
            Scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            Scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            JButton opretJournal = new JButton("Opret journal");

            listPanel.add(Scroller);
            listPanel.add(opretJournal);

            opretJournal.addActionListener(e -> {
                p.opretJournal(tekst.getText());
                new PopUp().infoBox("Patientjournal blev oprettet.");
                Patient.savePatientList();
                listFrame.dispose();
            });

            listFrame.getContentPane().add(BorderLayout.CENTER, listPanel);
            listFrame.setSize(500,400);
            listFrame.setVisible(true);
        }

        void drawLæsJournaler() {
            drawListFrame(true, false);
            JButton chooseButton = new JButton("Vælg");
            listPanel.add(chooseButton);
            chooseButton.addActionListener(e -> {
                String[] temp2 = navneListe.getSelectedValue().split("\\|");
                long id = Long.parseLong(temp2[1].substring(6,16));
                for(Patient p : Patient.patientListe) {
                    if(p.getCpr() == id) {
                        JFrame readFramePopUp = new JFrame();
                        readFramePopUp.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        JPanel readPanelPopUp = new JPanel();
                        JTextArea display = new JTextArea(10,20);
                        display.setLineWrap(true);
                        display.setEditable(false);
                        display.setText(p.læsJournal());
                        JScrollPane dispScroller = new JScrollPane(display);
                        dispScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        dispScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                        readPanelPopUp.add(dispScroller);
                        readFramePopUp.setContentPane(readPanelPopUp);
                        readFramePopUp.setSize(640,500);
                        readFramePopUp.setVisible(true);
                        listFrame.dispose();
                    }
                }
            });

            refreshListFrame();
        }

        void drawBookSeng() {
            drawListFrame(false, true);
            JButton chooseButton = new JButton("Vælg");
            listPanel.add(chooseButton);
            chooseButton.addActionListener(e -> {
                String[] temp = navneListe.getSelectedValue().split("#");
                int id = Integer.parseInt(temp[1]);
                for(Seng s : Seng.sengeStue1) {
                    if(s.getId() == id) {
                        listFrame.dispose();
                        drawListFrame(false,false);
                        JButton vælgPatient = new JButton("Vælg");
                        listPanel.add(vælgPatient);
                        vælgPatient.addActionListener(ev -> {
                            String[] temp2 = navneListe.getSelectedValue().split("\\|");
                            long id2 = Long.parseLong(temp2[1].substring(6,16));
                            for(Patient p : Patient.patientListe) {
                                if(p.getCpr() == id2) {
                                    new PopUp().infoBox("Du bookede seng #"+id+" til patienten "+p.getNavn());
                                    Seng.bookSeng(s, p);
                                    Seng.saveSengeList();
                                    Patient.savePatientList();
                                    listFrame.dispose();
                                }
                            }
                        });
                        refreshListFrame();
                    }
                }
            });

            refreshListFrame();
        }

        void drawProfileButtons() {
            profileButtons.forEach(JButton -> {
                panel.add(Box.createVerticalStrut(50));
                panel.add(JButton);
            });
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
            listPanel.add(listScroller);
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

        void drawListFrame(boolean fraLæsJournal, boolean fraBookSeng) {
            listFrame = new JFrame();
            listFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            listFrame.setResizable(false);
            listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel,BoxLayout.Y_AXIS));
            String[] navneArray;
            JLabel chooseLabel = new JLabel();
            if(fraLæsJournal) {
                chooseLabel.setText("Vælg en patient:");
                ArrayList<Patient> temp = new ArrayList<>();
                for (Patient p : Patient.patientListe) if (p.harJournal()) temp.add(p);
                navneArray = temp.stream().map(p -> p.getNavn() + " | CPR: " + p.getCpr() + p.getSeng()).toArray(String[]::new);

            } else if (fraBookSeng){
                chooseLabel.setText("Vælg en seng:");
                ArrayList<Seng> temp1 = new ArrayList<>();
                for(Seng s : Seng.sengeStue1) if(s.erLedig()) temp1.add(s);
                navneArray = temp1.stream().map(s -> "Seng #"+s.getId()).toArray(String[]::new);
            } else {
                chooseLabel.setText("Vælg en patient:");
                navneArray = Patient.patientListe.stream().map(p -> p.getNavn() + " | CPR: " + p.getCpr() + p.getSeng()).toArray(String[]::new);
            }
            navneListe = new JList<>(navneArray);
            JScrollPane listScroller = new JScrollPane(navneListe);
            listScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            listScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            listPanel.add(chooseLabel);
            listPanel.add(listScroller);
        }

        void refreshListFrame() {
            listFrame.getContentPane().add(BorderLayout.CENTER, listPanel);
            listFrame.setSize(500,500);
            listFrame.validate();
            listFrame.repaint();
            listFrame.setVisible(true);


        }

    }

    private void saveList() {
        serialize(brugerListe, "Data/brugerListe.ser");
        System.out.println("Liste blev gemt.");
    }

    private void loadList() {
        try {
            //noinspection unchecked
            brugerListe = (ArrayList<Bruger>) deserialize("Data/brugerListe.ser");
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

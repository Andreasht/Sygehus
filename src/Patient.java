import static andUtils.Serializer.*;

import java.io.File;
import java.util.ArrayList;

public class Patient implements java.io.Serializable {
    public static ArrayList<Patient> patientListe = new ArrayList<>();
    private String navn;
    private boolean erIndlagt;
    private String indlæggelsesGrund;
    private long cpr;
    private boolean harJournal = false;
    private Journal journal;
    private boolean iSeng = false;
    private Seng seng;

    Patient(String n, long i) {
        navn = n;
        cpr = i;
    }

    static void indskriv(Patient p, String grund) {
        patientListe.add(p);
        p.indlæggelsesGrund = grund;
        p.erIndlagt = true;
    }

    static void udskriv(Patient p) {
        patientListe.remove(p);
        p.erIndlagt = false;
        p.indlæggelsesGrund = "Ikke indlagt";
        p.seng.setLedig(true);
        p.setSeng(null);
        p.iSeng = false;

    }

    public String getNavn() {
        return navn;
    }

    public String getGrund() {
        return indlæggelsesGrund;
    }

    static void savePatientList() {
        serialize(patientListe,"Data/patientListe.ser");
        System.out.println("Patientliste blev gemt.");
    }

    static void loadPatientList() {
        try {
            File listFile = new File("Data/patientListe.ser");
            if (!listFile.exists()) {
                System.out.println("Patientliste ikke fundet. Går videre.");
            } else {
                System.out.println("Patient liste fundet. Loader.");
                patientListe = (ArrayList<Patient>) deserialize("Data/patientListe.ser");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Noget gik galt under load af patientliste");
            e.printStackTrace();
        }
    }

    public long getCpr() {
        return cpr;
    }

    public boolean harJournal() {
        return harJournal;
    }

    public void setHarJournal(boolean harJournal) {
        this.harJournal = harJournal;
    }

    void opretJournal(String in) {
        journal = new Journal(this, in);
        this.setHarJournal(true);
    }

    void setISeng(boolean b) {
        iSeng = b;
    }

    boolean iSeng() {
        return iSeng;
    }


    String læsJournal() {
        return journal.readJournal();
    }

    public String getSeng() {
        if(iSeng) {
            return " | Seng #"+seng.getId();
        } else {
            return "";
        }
    }

    void setSeng(Seng s) {
        seng = s;
    }
}

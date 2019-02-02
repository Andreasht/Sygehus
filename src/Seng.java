import java.io.File;
import java.util.ArrayList;
import static andUtils.Serializer.*;

public class Seng implements java.io.Serializable {
    private int id;
    private boolean ledig = true;
    private Patient patient;

    static ArrayList<Seng> sengeStue1 = new ArrayList<>();

    private Seng(int i) {
        id = i;
    }
    static {
        File sengeListe = new File("sengeListe.ser");
        if(!sengeListe.exists()) {
            System.out.println("Sengeliste bliver initialiseret.");
            for (int i = 1; i < 11; i++) {
                sengeStue1.add(new Seng(i));
            }

        } else {
            System.out.println("Sengeliste bliver loadet.");
            try {
                sengeStue1 = (ArrayList<Seng>) deserialize("Data/sengeListe.ser");
            } catch(Exception ex) {
                System.out.println("Noget gik galt under load af sengeliste.");
            }

        }

    }

    static void bookSeng(Seng s, Patient p) {
        s.setPatient(p);
        s.setLedig(false);
        p.setISeng(true);
        p.setSeng(s);
    }

    void setPatient(Patient p) {
        patient = p;
    }

    void setLedig(boolean b) {
        ledig = b;
        patient = null;
    }

    int getId() {
        return id;
    }

    boolean erLedig() {
        return ledig;
    }


    public Patient getPatient() {
        return patient;
    }

    static void saveSengeList() {
        serialize(sengeStue1, "Data/sengeListe.ser");
    }

}

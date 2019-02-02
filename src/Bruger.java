import java.util.Arrays;

public abstract class Bruger implements java.io.Serializable {
    private Status status;
    static int antalBrugere;
 //   private int id;
    private String navn;
    private String brugernavn;
    private char[] kodeord;
    static final String[] ROLE_LIST = {"Admin","Læge","Sygeplejerske","Personel"};

    Bruger() {
        navn = "Test";
        status = Status.OPT;
    }

    Bruger(String n, String bN, char[] k) {
        navn = n;
        brugernavn = bN;
        kodeord = k;
   //     id = antalBrugere + 1;
        antalBrugere++;
        status = Status.LEDIG;
    }

    public String getBrugernavn() {
        return brugernavn;
    }

    public String getNavn() {
        return navn;
    }

  /*  public int getId() {
        return id;
    } */

    boolean authenticate(char[] kodeIn) {
        return Arrays.equals(this.kodeord, kodeIn);
    }

    String getStatus() {
        return status.getText();
    }

    void setStatus(int stat) {
        switch(stat) {
            case 0: status = Status.LEDIG; break;
            case 1: status = Status.OPT; break;
            case 2: status = Status.FRAV; break;
            case 3: status = Status.PAUSE; break;
            default: System.out.println("Der skete en fejl i setStatus()"); break;
        }
    }

    String getRolle() {
        return getClass().toString().substring(6);
    }

    public static void main(String[] args) {

    }
}
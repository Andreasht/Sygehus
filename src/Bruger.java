import java.util.Arrays;

class Bruger implements java.io.Serializable {
    Status status;
    private static int antalBrugere;
    private int id;
    private String navn;
    private String brugernavn;
    private char[] kodeord;
    private String rolle;
    static final String[] ROLE_LIST = {"Admin","Læge","Sygeplejerske","Andet"};

    Bruger() {
        id = antalBrugere + 1;
        antalBrugere++;
    }

    Bruger(String n, String bN, char[] k, String r) {
        navn = n;
        brugernavn = bN;
        kodeord = k;
        rolle = r;
        id = antalBrugere + 1;
        antalBrugere++;
    }

    public String getBrugernavn() {
        return brugernavn;
    }

    public String getNavn() {
        return navn;
    }

    public int getId() {
        return id;
    }

    boolean authenticate(char[] kodeIn) {
        return Arrays.equals(this.kodeord, kodeIn);
    }

    public String getRolle() {
        return rolle;
    }
}
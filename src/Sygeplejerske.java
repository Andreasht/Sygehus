public class Sygeplejerske extends Bruger {
    public static final char[] DEF_SYGP_KODE = {'s','y','g'};

    Sygeplejerske(String n, String bN, char[] k) {
        super(n, bN, k);
        Bruger.antalBrugere++;
    }
}

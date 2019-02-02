public class Læge extends Bruger {
    public static final char[] DEF_LÆGE_KODE = {'l','æ','g','e'};

    Læge(String n, String bN, char[] k) {
        super(n, bN, k);
        Bruger.antalBrugere++;
    }
}

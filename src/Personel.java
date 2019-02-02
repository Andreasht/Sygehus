public class Personel extends Bruger {
    public static final char[] DEF_PERS_KODE = {'p','e','r','s'};

    Personel(String n, String bN, char[] k) {
        super(n, bN, k);
        Bruger.antalBrugere++;
    }

}

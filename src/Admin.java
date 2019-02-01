public class Admin extends Bruger {
    static final char[] DEF_ADMIN_KODE = {'a','d','m','i','n'};

    Admin(String n, String bN, char[] k) {
        super(n, bN, k);
        Bruger.antalBrugere++;
    }

    void adminTest() {
        System.out.println("AdminTest");
    }
}

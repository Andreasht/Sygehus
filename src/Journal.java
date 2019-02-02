import java.io.File;

import static andUtils.FileScanner.*;

@SuppressWarnings("Duplicates")
public class Journal implements java.io.Serializable {
    private static final File JOURNAL_DIR = new File("Journaler");
    private File journalMappe;
    private String content;
    private String finalPath;

    Journal(Patient p, String in) {
        if(!JOURNAL_DIR.exists()) JOURNAL_DIR.mkdir();
        content = in;
        String personligMappePath = JOURNAL_DIR+"/"+p.getNavn();
        journalMappe = new File(personligMappePath);
        finalPath = personligMappePath+"/"+p.getNavn()+".txt";
        opretJournal();
    }

    private void opretJournal() {
        if(!journalMappe.exists()) {
            System.out.println("Journal ikke fundet. Mappe bliver lavet...");
            journalMappe.mkdir();
        }
        writeToFile(finalPath, content);
        System.out.println("Skrev til fil.\nJournal oprettet.");
    }

    String readJournal() {
        return readFromFile(finalPath);
    }
}

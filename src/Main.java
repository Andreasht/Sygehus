import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import andUtils.*;

public class Main {
	private ArrayList<Bruger> brugerListe = new ArrayList<>();
	private static Scanner s = new Scanner(System.in);

	private static void print(ArrayList<Dyr> list) {
		if (!(dyrList.isEmpty())) {
			for(Dyr d : list) {
				System.out.println(d.getInfo());
			}
		} else {
			System.out.println("Der er ingen dyr i systemet.");
		}
	}

	private static void opret() {
		System.out.println("Hvilken art er dyret?");
		String artIn = s.nextLine();
		System.out.println("Hvad hedder dyret?");
		String navnIn = s.nextLine();
		System.out.println("Hvad er dyrets alder?");
		String alderIn = s.nextLine();
		System.out.println("Hvorfor er dyret indlagt?");
		String fejlIn = s.nextLine();
		dyrList.add(new Dyr(navnIn,artIn,alderIn,fejlIn));
		System.out.println(alderIn+" år gamle "+navnIn+" af arten "+artIn+" blev oprettet.\nGrund til indlæggelse: "+fejlIn);
	}
	private void load() {
		try {
			brugerListe = (ArrayList<Bruger>) Serializer.deserialize("brugerListe.ser");
		} catch (IOException e) {
			System.out.println("Ingen gemt data.");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}


	}

	private static void gem() {
		System.out.println("Gemmer...");
		StringBuilder b = new StringBuilder();
		for(Dyr d : dyrList) {
			b.append(d.getInfo()+",");
		}
		String out = b.toString();
		FileScanner.writeOverwrite(out);
		System.out.println("Data gemt.");
	}

	public static void main(String[] args) {
		load();
		Scanner s = new Scanner(System.in);
		System.out.println("Velkommen til VetOS v0.47\nSkriv 'start' for at starte.");
		String input = s.nextLine();
		while(!(input.equals("sluk"))) {
			System.out.println("Indtast kommando:");
			String choice = s.nextLine();
			if (choice.equals("opret")) {
				opret();
			} else if (choice.equals("liste")) {
				print(dyrList);
			} else if (choice.equals("gem")) {
				gem();
			} else if (choice.equals("cls")){
				cls();
			} else if (choice.equals("reset")) {
				reset();
			} else if (choice.equals("hjælp")) {
				System.out.println("Du kan bruge følgende kommandoer:"
						+ "\n "
						+ "\nopret - Tilføj et nyt dyr til systemet."
						+ "\nliste - Vis en liste over dyrene i systemet."
						+ "\ngem - Gem data."
						+ "\nsluk - Sluk systemet."
						+ "\nhjælp - Vis denne side."
						+ "\ncls - Ryd terminalen"
						+ "\nreset - Ryd liste over dyrene i systemet - ADVARSEL: KAN IKKE FORTRYDES!"
						+ "\n ");
			} else if (choice.equals("sluk")) {
				break;
			} else {
				System.out.println("Ugyldig kommando. Skriv 'hjælp' for en liste af kommandoer.");
			}
		}

		s.close();
	}
}

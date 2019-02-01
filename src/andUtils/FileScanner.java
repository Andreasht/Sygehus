package andUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

class FileScanner {

	public static String readFromFile(String f) {
		String read = "";
		try {
			read = new String(Files.readAllBytes(Paths.get(f)));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
        return read;
    }
	
	public static void writeToFile(String s, String f) {

		try {
			Writer writer = new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8);
			BufferedWriter fout = new BufferedWriter(writer);
			fout.write(s);
			fout.newLine();
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<String> readEachLine(String f) {
		ArrayList<String> list = new ArrayList<>();
		File file = new File(f);
		if(file.exists()) {
			try {
				list = (ArrayList<String>) Files.readAllLines(file.toPath(),Charset.defaultCharset());
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public static ArrayList<Integer> readEachInt(String f) {
		return Utils.toIntArray(readEachLine(f));
	}

}

package translate.application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TextArchiver {

	static void addToFile(String text) {

		text = collapseWhitespace(text);
		
		File file = new File("translation");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try (PrintWriter writer = new PrintWriter(file)) {
			writer.write(text);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (FileWriter writer = new FileWriter("translation")) {
			writer.write(text);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static String collapseWhitespace(String value) {
		return value.replaceAll("\\s+", " ");
	}
}

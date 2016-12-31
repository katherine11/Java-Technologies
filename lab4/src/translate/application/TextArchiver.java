package translate.application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

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

		// try(FileOutputStream oStream = new FileOutputStream("translation")){
		//
		// oStream.write(text.getBytes());
		// oStream.flush();
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		// writes the last line only:
		// try (PrintStream out = new PrintStream(new
		// FileOutputStream("filename.txt"))) {
		// out.print(text);
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }

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

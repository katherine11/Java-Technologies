package translate.application;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Translator {

	private static final String TRANSLATION_FILE_NAME = "translation_to_cyrilic";
	private Map<Object, Object> pairs = new HashMap<>();
	private int wordsCount = 0;
	
	public Translator() {
		generateFileContent();
	}

	private void generateFileContent() {
		Properties properties = new Properties();

		try (InputStream inputStream = new FileInputStream("character-mappings.properties")) {

			properties.load(inputStream);

			Set<Object>keys = new HashSet<>(properties.keySet());

			for (Object key : keys) {
				this.pairs.put(key, properties.get(key));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void translate(String text) throws TranslatorException {

		StringBuilder translation = new StringBuilder();

		for (int index = 0; index < text.length(); index++) {

			char symbol = text.charAt(index);

			for (Object key : this.pairs.keySet()) {
				if (toChar(key) == symbol) {
					translation.append(this.pairs.get(key));
					break;
				}
			}
		}

		String toString = translation.toString();
		System.out.println(toString);
		TextArchiver.addToFile(toString);
		
		wordsCount += getWordsCount(text, "translation");

	}
	
	
	long getWordsCount(String text, String fileName) {
		
		String[] words = ContentAnalyser.splitText();
		
		return words.length;
	}


	private char toChar(Object key) throws TranslatorException {
		if (key instanceof String) {
			return ((String) key).charAt(0);
		}
		throw new TranslatorException("This object cannot be casted to string!");
	}

	public int getWordsCount() {
		return wordsCount;
	}
	
	
	
}

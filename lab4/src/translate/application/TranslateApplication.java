package translate.application;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TranslateApplication {

	private static final String REGEX_STRING = "(.*\\s+)?kraj(\\s+|\n+|.*)?$";
	
	private Translator translator;
	private ContentAnalyser analyser;
	private List<String> text = new LinkedList<String>();

	public TranslateApplication(Translator translator, ContentAnalyser analyser) {
		this.translator = translator;
		this.analyser = analyser;
	}

	void startApplication() throws TranslatorException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter text: ");

		while (true) {
			String string = scanner.nextLine();

			text.add(string);

			if (checkForEnd(string)) {
				break;
			}

		}

		for (String string : text) {
			translator.translate(string);
		}
		
		System.out.println("Words: " + translator.getWordsCount());

		
		System.out.println("First seven most repeated words: ");
		analyser.printFirstSeven(text.toString());

	}
	
	static void printText(List<String> text) {
		for (String string : text) {
			System.out.println(string);
		}
	}

	private static boolean checkForEnd(String string) {
	
		if(Pattern.compile(REGEX_STRING).matcher(string).matches()){
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws Exception {

		Translator translator = new Translator();
		ContentAnalyser analyser = new ContentAnalyser();
		TranslateApplication application = new TranslateApplication(translator, analyser);

		application.startApplication();
		
		
	}

}

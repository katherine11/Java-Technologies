package translate.application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ContentAnalyser {

	private static final String REGEX = "\\s+|\\?|\\.|\\!|\\s+";

	Map<Character, Integer> letterRepeatings = new HashMap<Character, Integer>();

	void analyseLettersRepeating() {

		String text = getText();

		text = text.toLowerCase();

		for (int index = 0; index < text.length(); index++) {

			char symbol = text.charAt(index);
			int startingValue = 1;
			if (!letterRepeatings.containsKey(symbol)) {
				letterRepeatings.put(symbol, startingValue);
			} else {
				letterRepeatings.put(symbol, letterRepeatings.get(symbol) + 1);
			}

		}

	}

	private String getText() {
		String[] words = splitText();

		StringBuilder builder = new StringBuilder();
		for (String s : words) {
			builder.append(s);
		}

		String text = builder.toString();
		return text;
	}

	public static String[] splitText() {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader("translation"))) {
			sb.append(br.readLine());
		} catch (Exception e) {
			e.printStackTrace();
		}

		String string = sb.toString();

		String[] words = string.split(REGEX);
		return words;
	}

	void printFirstSeven(String text) {

		analyseLettersRepeating();

		// convert the original map to map with integer and list from character:

		Map<Integer, List<Character>> result = exchangeKeysAndValues();

		// print first seven:

		List<Integer> keys = new ArrayList<>(result.keySet());
		int count = 0;
		for (int index = keys.size() - 1; index >= 0 && count <= 7; index--) {
			for (Character character : result.get(keys.get(index))) {
				if (count >= 7) {
					break;
				}
				System.out.print(keys.get(index) + " - ");
				System.out.println(character);
				count++;
			}
		}

	}

	private Map<Integer, List<Character>> exchangeKeysAndValues() {
		Map<Integer, List<Character>> result = new TreeMap<>();

		for (Character symbol : letterRepeatings.keySet()) {

			List<Character> list = new ArrayList<>();
			list.add(symbol);

			int newKey = letterRepeatings.get(symbol);

			if (!result.containsKey(newKey)) {

				result.put(newKey, list);
			} else {
				result.get(newKey).add(symbol);
				result.put(newKey, result.get(newKey));
			}

		}
		return result;
	}

}

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Demo {

	static void start() {
		System.out.println("Enter the length of the string: ");
		Scanner scanner = new Scanner(System.in);

		int length = scanner.nextInt();

		Set<Integer> toGuess = generateGuess(length);

		System.out.println("Generated number: ");
		toGuess.forEach(i -> System.out.print(i + " "));
		
		Set<Integer> guess = new LinkedHashSet<>(length);

		System.out.println();
		System.out.println("Enter a number to guess:");
		for (int index = 0; index < length; index++) {
			guess.add(scanner.nextInt());
		}

		guess.forEach(i -> System.out.print(i + " "));
		System.out.println();
		
		Game<Integer> game = new Game<Integer>((byte) length);

		try {
			Map.Entry<Byte, Byte> guessResult = game.check(guess);
			System.out.println(guessResult.getKey() + " " + guessResult.getValue());
		} catch (GameException e) {
			e.printStackTrace();
			return;
		}
	}


	private static Set<Integer> generateGuess(int length) {
		Set<Integer> result = new LinkedHashSet<>(length);
		while(result.size() < length){
			int random = (int) (Math.random() * 10);
			if(!result.contains(random)){
				result.add(new Integer(random));
			}
		}
		
		return result;
	}

	public static void main(String[] args) {
		start();

	}

}

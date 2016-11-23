import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class Demo {

	public static void main(String[] args) {
		
		System.out.println("Enter the length of the string: ");
		Scanner scanner = new Scanner(System.in);
		
		int length = scanner.nextInt();
		
		
		Set<Object> objectToBeGuessed = new LinkedHashSet<Object>(length);
		for (int index = 0; index < length; index++) {
			objectToBeGuessed.add(new Object());
			
		}
		
		//Game<Object> game = new Game<Object>(length, stringToBeGuessed);
		
		
		

	}

}

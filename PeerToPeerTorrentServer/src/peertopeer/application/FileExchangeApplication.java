package peertopeer.application;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import peertopeer.ecxeptions.PeerClientException;


public class FileExchangeApplication {

	private static final String EXIT_PATETRN = "\\s*exit\\s*";
	private static final String SEED = "seed";
	private static final String UNREGISTER = "unregister";
	private static final String LIST_FILES = "list";
	private static final String SEARCH = "search";
	private static final String DOWNLOAD = "download";

	static void menu(User user) throws ClientException, PeerClientException {

		System.out.println("Logged in as user:  " + user.getUsername());
		Scanner scanner = new Scanner(System.in);
		String input;

		
		while (true) {
			
			displayMenu();
			
			System.out.println();
			System.out.print("Input: ");
			input = scanner.nextLine();

			
			if (matches(input)) {
				System.out.println("Exit!");
				break;
			}

			// seed directory:
			if (input.startsWith(SEED)) {

				String directory = removeWhiteSpace(input.substring(SEED.length()));
				if (directory != "") {
					user.seed(directory);
				}
			}
			
			
			//unregister files;
			if (input.startsWith(UNREGISTER)) {

				String[] files = input.substring(UNREGISTER.length()).split("\\s+");
				
				if(files.length > 0){
					
					Set<String> filesSet = new HashSet<>(Arrays.asList(files));
					
					filesSet.forEach(file -> removeWhiteSpace(file));
					
					user.unregister(filesSet);
				}
			}
			
			//list files;
			if(input.startsWith(LIST_FILES)){
				
				if(input.substring(LIST_FILES.length()) == ""){
					user.listFiles();
				}
			}
			
			//search files;
			if(input.startsWith(SEARCH)){
				
				String[] files = input.substring(SEARCH.length()).split("\\s+");
				
				if(files.length > 0){
					
					Set<String> fileNames = new HashSet<>(Arrays.asList(files));
					
					fileNames.forEach(file -> removeWhiteSpace(file));
					
					user.search(fileNames);
				}
				
				
			}
			
			//download file; -> user path path
			if(input.startsWith(DOWNLOAD)){
				String[] components = input.substring(DOWNLOAD.length()).split("\\s+");
		
				try {
					user.download(components[0], components[1], components[2]);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}	
			}
		}

	}

	private static void displayMenu() {
		
		System.out.println("Choose a command: ");
		System.out.println();
		
		System.out.println("1) exit - exit the menu");
		System.out.println("2) seed directory - seed default directory");
		System.out.println("3) unregister file1 file2 ... filen - unregister files");
		System.out.println("4) list - list files");
		System.out.println("5) search file1 file2 ... filen - search files");
		System.out.println("6) download username file - download file from a specific user");
		
	}

	private static boolean matches(String exitPatetrn) {
		
		return Pattern.compile(EXIT_PATETRN).matcher(exitPatetrn).matches();
	}

	private static String removeWhiteSpace(String string) {
		return string = string.replaceAll("\\s+", "");
	}

	public static void main(String[] args) throws UserException {

		//create a few clients;
		User client1 = new User("user1", "127.0.0.1", 4444);
		User client2 = new User("user2", "127.0.0.1", 4445);
		User client3 = new User("user3", "127.0.0.1", 4446);
		User client4 = new User("user4", "127.0.0.1", 4447);
		User client5 = new User("user5", "127.0.0.1", 4448);
		User client6 = new User("user6", "127.0.0.1", 4449);
	
		try {
			menu(client1);
		} catch (ClientException | PeerClientException e) {
			e.printStackTrace();
			return;
		}
//		menu(client2);
//		menu(client3);

	}
}

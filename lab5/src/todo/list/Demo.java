package todo.list;

import java.util.Scanner;

public class Demo {
	
	private static final String PATH_TO_FILE = "exports";
	private static final int FIRST_VALUE = 1;
	private static final int EXIT_VALUE = 6;
	private static final int PERIOD_OF_UPCOMING_TASKS = 3;

	
	public static void start(ToDoList list) {

		Scanner scanner = new Scanner(System.in);

		byte option;

		do {

			System.out.println("Choose an option: ");
			System.out.println("1) All tasks ordered by priority: ");
			System.out.println("2) Tasks, having status IN PROGRESS: ");
			System.out.println("3) Upcoming tasks with a 3-day deadline: ");
			System.out.println("4) Import tasks from a file: ");
			System.out.println("5) Export tasks from a file: ");
			System.out.println("6) Exit");

			System.out.println("Choose between 1 and 6: ");
			option = scanner.nextByte();

			switch (option) {

			case 1:
				list.getTasksOrderedByPriority();
				break;
			case 2:
				list.getTasksWithStatus(TaskStatus.IN_PROCESS);
				break;
			case 3:
				list.getTasksWithDeadline((byte) PERIOD_OF_UPCOMING_TASKS);
				break;
			case 4:
				if(list.importTasksFromFile(PATH_TO_FILE)){
					System.out.println("Tasks imported.");
				}
				else{
					System.out.println("Tasks not imported.");
				}
				break;
			case 5:
				list.exportTasksToFile(PATH_TO_FILE);
				break;
			case 6:
				System.out.println("Exiting...");
				break;
			default:
				System.out.println("There is not such an option!");
			}

		} while ((option < FIRST_VALUE || option > EXIT_VALUE) || option != EXIT_VALUE);

	}

	public static void main(String[] args) {
		ToDoList list = new ToDoList();
		start(list);
	}

}

package todo.list;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class ToDoList {

	private static final int PERIOD_OF_UPCOMING_TASKS = 3;
	private static final int NUMBER_OF_TASKS = 7;
	Task[] tasks = new Task[NUMBER_OF_TASKS];

	public ToDoList() {

		String[] names = { "Clean", "Go to fitness", "To pay the bills"};

		for (int index = 0; index < this.tasks.length; index++) {

			String randomName = names[(int) (Math.random() * names.length)];
			TaskStatus randomStatus = TaskStatus.values()[(int) (Math.random()*TaskStatus.values().length)];
			byte randomPriority = (byte) (Math.random() * 5 + 1);
			LocalDate randomDate = LocalDate.now().plusDays((int) (Math.random() * 10 + 2));

			this.tasks[index] = new Task(randomName, randomStatus, randomPriority, randomDate);
		}
	}

	void printTasksOrderedByPriority() {

		Task[] tasksCopy = this.tasks.clone();

		Arrays.sort(tasksCopy);

		printTasks(tasksCopy);

	}

	void printTasksWithStatus(TaskStatus status) {

//		Task[] tasksCopy = new Task[NUMBER_OF_TASKS];
		
		int resultLength = 0;
		for (int index = 0; index < this.tasks.length; index++) {			
			if (this.tasks[index].getStatus().equals(status)) {
//				tasksCopy[taskIndex++] = this.tasks[index];
				resultLength++;
			}
		}
		
		Task [] result = returnTaskStatusResult(this.tasks, resultLength);
		Arrays.sort(result);
		
		printTasks(result);

	}

	private Task[] returnTaskStatusResult(Task[] tasksCopy, int length) {
		Task [] result = new Task[length];
		
		int taskIndex = 0;
		
		for (int index = 0; index < tasksCopy.length; index++) {
			if(tasksCopy[index].getStatus().equals(TaskStatus.IN_PROCESS)){
				result[taskIndex++] = tasksCopy[index];
			}
		}
		
		return result;
	}

	void tasksWithDeadline(byte numberOfDays) {

		Task[] tasksCopy = new Task[NUMBER_OF_TASKS];

		for (int index = 0; index < this.tasks.length; index++) {

			LocalDate currentDeadline = this.tasks[index].getDeadline();

			if (checkForValidTask(numberOfDays, index, currentDeadline)) {
				tasksCopy[index] = this.tasks[index];
			}

		}

		printTasks(tasksCopy);

	}

	private boolean checkForValidTask(byte numberOfDays, int index, LocalDate currentDeadline) {
		return currentDeadline.isBefore(currentDeadline.plusDays(numberOfDays))
				&& (this.tasks[index].getStatus().equals(TaskStatus.INITIAL)
						|| this.tasks[index].getStatus().equals(TaskStatus.IN_PROCESS));
	}

	void printTasks(Task[] tasksToBePrinted) {
		for (Task task : tasksToBePrinted) {
			System.out.println(task);
		}
	}

	public void start() {

		Scanner scanner = new Scanner(System.in);

		byte option;

		do {
			System.out.println("Choose an option: ");
			System.out.println("1) All tasks ordered by priority: ");
			System.out.println("2) Tasks, having status IN PROGRESS: ");
			System.out.println("3) Upcoming tasks with a 3-day deadline: ");
			System.out.println("4) Exit");

			System.out.println("Choose between 1 and 4: ");
			option = scanner.nextByte();
			
			switch (option) {

			case 1:
				printTasksOrderedByPriority();
				break;
			case 2:
				printTasksWithStatus(TaskStatus.IN_PROCESS);
				break;
			case 3:
				tasksWithDeadline((byte) PERIOD_OF_UPCOMING_TASKS);
				break;
			case 4: 
				System.out.println("Exiting...");
				break;
			default:
				System.out.println("There is not such an option!");
			}
		
		} while (option != 4 || option < 1 || option > 4);
		

	}

}

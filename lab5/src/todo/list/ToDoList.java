package todo.list;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ToDoList {

	private static final int FIRST_VALUE = 1;
	private static final int EXIT_VALUE = 6;
	private static final int PERIOD_OF_UPCOMING_TASKS = 3;
	private static final int NUMBER_OF_TASKS = 7;
	private static final long DAY_IN_MILLIS = 86_400_000;

	List<Task> tasks = new ArrayList<Task>();

	public ToDoList() {

		List<String> names = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));

		for (int index = 0; index < 10; index++) {
			String randomName = names.get((int) (Math.random() * names.size()));
			TaskStatus randomStatus = TaskStatus.values()[(int) (Math.random() * TaskStatus.values().length)];
			byte randomPriority = (byte) (Math.random() * 5 + 1);
			LocalDate randomDate = LocalDate.now().plusDays((int) (Math.random() * 10 + 2));

			Task task = new Task(randomName, randomStatus, randomPriority, randomDate);
			tasks.add(task);
		}

	}

	void printTasksOrderedByPriority() {

		List<Task> tasksCopy = new ArrayList<>(this.tasks);

		Collections.sort(tasksCopy);

		printTasks(tasksCopy);

	}

	void printTasksWithStatus(TaskStatus status) {

		int resultLength = 0;
		for (Iterator<Task> iterator = tasks.iterator(); iterator.hasNext(); iterator.next()) {

			if (iterator.next().getStatus().equals(status)) {
				resultLength++;
			}
		}

		List<Task> result = returnTaskStatusResult(this.tasks, resultLength, status);
		Collections.sort(result);
		printTasks(result);

	}

	private List<Task> returnTaskStatusResult(List<Task> tasksCopy, int length, TaskStatus status) {
		List<Task> result = new ArrayList<Task>();

		for (Iterator<Task> iterator = tasksCopy.iterator(); iterator.hasNext(); iterator.next()) {

			Task next = iterator.next();

			if (next.getStatus().equals(status)) {
				result.add(next);
			}
		}

		return result;
	}

	void tasksWithDeadline(byte numberOfDays) {

		int resultLength = 0;

		for (Iterator<Task> iterator = tasks.iterator(); iterator.hasNext(); iterator.next()) {
			Task next = iterator.next();
			LocalDate currentDeadline = next.getDeadline();

			if (checkForValidTask(numberOfDays, next, currentDeadline)) {
				resultLength++;
			}

		}

		List<Task> result = returnUpcomingTasksResult(this.tasks, resultLength, numberOfDays);
		Collections.sort(result);
		printTasks(result);

	}

	private List<Task> returnUpcomingTasksResult(List<Task> tasksCopy, int length, byte numberOfDays) {
		List<Task> result = new ArrayList<>();

		for (Iterator<Task> iterator = tasks.iterator(); iterator.hasNext(); iterator.next()) {

			Task next = iterator.next();

			LocalDate currentDeadline = next.getDeadline();

			if (checkForValidTask(numberOfDays, next, currentDeadline)) {
				result.add(next);
			}
		}

		return result;
	}

	private boolean checkForValidTask(byte numberOfDays, Task currentTask, LocalDate currentDeadline) {

		LocalDate today = LocalDate.now();

		return today.plusDays(numberOfDays).compareTo(currentDeadline) < 0
				&& (currentTask.getStatus().equals(TaskStatus.INITIAL)
						|| currentTask.getStatus().equals(TaskStatus.IN_PROCESS));
	}

	void printTasks(List<Task> tasksToBePrinted) {
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
			System.out.println("4) Import tasks from a file: ");
			System.out.println("5) Export tasks from a file: ");
			System.out.println("6) Exit");

			System.out.println("Choose between 1 and 6: ");
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
				importTasksFromFile("exports");
				break;
			case 5:
				exportTasksToFile("exports");
				break;
			case 6:
				System.out.println("Exiting...");
				break;
			default:
				System.out.println("There is not such an option!");
			}

		} while ((option < FIRST_VALUE || option > EXIT_VALUE) || option != EXIT_VALUE);

	}

	private void exportTasksToFile(String fileName) {

		if (fileName != null) {

			Path pathToFile = Paths.get(fileName);

			if (Files.exists(Paths.get(fileName), LinkOption.NOFOLLOW_LINKS)) {

				try {
					FileTime lastModifiedTime = Files.getLastModifiedTime(pathToFile, LinkOption.NOFOLLOW_LINKS);

					long differenceInHours = System.currentTimeMillis() - lastModifiedTime.toMillis();

					if (differenceInHours < DAY_IN_MILLIS) {
						Files.copy(pathToFile, Paths.get(fileName + "_copy " + LocalDateTime.now()));
					}

					else {

						try (FileInputStream in = new FileInputStream(fileName)) {

							Files.createDirectories(Paths.get(LocalDate.now().toString()));

							try (ZipOutputStream out = new ZipOutputStream(
									new FileOutputStream(LocalDate.now().toString() + "/Backup.zip"))) {

								out.putNextEntry(new ZipEntry(pathToFile + "_copy"));

								byte[] b = new byte[1024];
								int count;

								while ((count = in.read(b)) > 0) {
									out.write(b, 0, count);
								}
							}
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
					return;
				}

			}

			try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {

				for (Task task : tasks) {

					writer.write(task.toString());
					writer.newLine();
				}

			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}

		System.out.println("Export done!");

	}

	private void importTasksFromFile(String fileName) {
		if (fileName != null) {

			tasks.clear();

			String line;

			try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {

				while ((line = reader.readLine()) != null) {

					String[] fields = line.split(",");

					String taskName = fields[0];
					TaskStatus status = TaskStatus.valueOf(fields[1]);
					int priority = Integer.parseInt(fields[2]);
					LocalDate deadline = LocalDate.parse(fields[3]);

					Task currentTask = new Task(taskName, status, (byte) priority, deadline);

					this.tasks.add(currentTask);

				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("Import done!");
		}
	}

}

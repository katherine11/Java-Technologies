package todo.list;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.print.attribute.standard.RequestingUserName;

public class ToDoList {

	private static final long DAY_IN_MILLIS = 86_400_000;

	List<Task> tasks = new ArrayList<Task>();

	public ToDoList() {

		tasks.add(new Task("A", TaskStatus.IN_PROCESS, (byte) 3, LocalDate.now()));
		tasks.add(new Task("B", TaskStatus.DONE, (byte) 2, LocalDate.now().plusDays(3)));
		tasks.add(new Task("C", TaskStatus.INITIAL, (byte) 2, LocalDate.now().minusDays(4)));
		tasks.add(new Task("D", TaskStatus.IN_PROCESS, (byte) 1, LocalDate.now().plusMonths(2)));
		tasks.add(new Task("E", TaskStatus.DONE, (byte) 5, LocalDate.now().minusWeeks(3)));

	}

	List<Task> getTasksOrderedByPriority() {

		List<Task> tasksCopy = new ArrayList<>(this.tasks);

		Collections.sort(tasksCopy);

		printTasks(tasksCopy);

		return tasksCopy;
	}

	List<Task> getTasksWithStatus(TaskStatus status) {

		List<Task> result = new ArrayList<>();

		for (Task task : tasks) {
			if (task.getStatus().equals(status)) {
				result.add(task);
			}
		}
		Collections.sort(result);
		printTasks(result);
		return result;
	}

	List<Task> getTasksWithDeadline(int deadlinePeriod) {
		List<Task> result = new ArrayList<>();
		for (Task task : tasks) {
			LocalDate currentDeadline = task.getDeadline();
			if (checkForValidTask((byte) deadlinePeriod, task, currentDeadline)) {
				result.add(task);
			}
		}
		Collections.sort(result);
		printTasks(result);
		return result;
	}

	boolean checkForValidTask(byte numberOfDays, Task currentTask, LocalDate currentDeadline) {

		LocalDate today = LocalDate.now();

		return today.plusDays(numberOfDays).compareTo(currentDeadline) < 0
				&& (currentTask.getStatus().equals(TaskStatus.INITIAL)
						|| currentTask.getStatus().equals(TaskStatus.IN_PROCESS));
	}

	boolean importTasksFromFile(String fileName) {
		if (fileName != null) {

			tasks.clear();

			String line;

			try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {

				while ((line = reader.readLine()) != null) {

					constructTask(line);

				}

				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	void constructTask(String line) {
		String[] fields = line.split(",");

		String taskName = fields[0];
		TaskStatus status = TaskStatus.valueOf(fields[1]);
		int priority = Integer.parseInt(fields[2]);
		LocalDate deadline = LocalDate.parse(fields[3]);

		Task currentTask = new Task(taskName, status, (byte) priority, deadline);

		this.tasks.add(currentTask);
	}

	boolean exportTasksToFile(String fileName) {

		if (fileName != null) {

			Path pathToFile = Paths.get(fileName);

			if (Files.exists(Paths.get(fileName), LinkOption.NOFOLLOW_LINKS)) {

				try {
					checkForLastModifiedTime(fileName, pathToFile);

				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}

			}
			else{
				return false;
			}

			writeToFile(fileName);
		}

		return false;

	}

	 void checkForLastModifiedTime(String fileName, Path pathToFile) throws IOException {
		FileTime lastModifiedTime = Files.getLastModifiedTime(pathToFile, LinkOption.NOFOLLOW_LINKS);

		long differenceInHours = System.currentTimeMillis() - lastModifiedTime.toMillis();

		if (differenceInHours < DAY_IN_MILLIS) {
			Files.copy(pathToFile, Paths.get(fileName + "_copy " + LocalDateTime.now()));
		}

		else {

			createZipFolder(fileName, pathToFile);
		}
	}

	boolean createZipFolder(String fileName, Path pathToFile) {
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
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	void writeToFile(String fileName) {
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

	void printTasks(List<Task> tasksToBePrinted) {
		for (Task task : tasksToBePrinted) {
			System.out.println(task);
		}
	}

}

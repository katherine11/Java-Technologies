package todo.list;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TestToDoApplication {

	private static final int DEADLINE_PERIOD = 4;

	@Test
	public void testTasksOrderedByPriority() {
		ToDoList list = new ToDoList();

		// get already sorted tasks:
		List<Task> expectedTasks = getTasksByPriority(list.tasks);

		list.tasks = list.getTasksOrderedByPriority();

		assertList(list.tasks, expectedTasks);

	}

	private List<Task> getTasksByPriority(List<Task> tasks) {

		List<Task> result = new ArrayList<>(tasks);
		Collections.sort(result);
		return result;

	}

	@Test
	public void testGetTasksByStatus() {

		ToDoList list = new ToDoList();

		final TaskStatus status = TaskStatus.IN_PROCESS;

		list.tasks = list.getTasksWithStatus(status);

		List<Task> expected = getTasksByStatus(list.tasks, status);

		assertList(list.tasks, expected);

	}

	private List<Task> getTasksByStatus(List<Task> tasks, TaskStatus status) {

		List<Task> result = new ArrayList<>();

		for (Task task : tasks) {
			if (task.getStatus().equals(status)) {
				result.add(task);
			}
		}

		return result;
	}

	@Test
	public void testTasksWithDeadline() {
		ToDoList list = new ToDoList();

		list.tasks = list.getTasksWithDeadline(DEADLINE_PERIOD);
		List<Task> expected = getTasksByDeadline(list, list.tasks, (byte) DEADLINE_PERIOD);

		assertList(list.tasks, expected);

	}

	private List<Task> getTasksByDeadline(ToDoList list, List<Task> tasks, byte deadline) {
		List<Task> result = new ArrayList<>();

		for (Task task : tasks) {
			LocalDate currentDeadline = task.getDeadline();
			if (list.checkForValidTask(deadline, task, currentDeadline)) {
				result.add(task);
			}
		}

		return result;
	}
	
	private void assertList(List<Task> tasks, List<Task> expectedTasks) {

		Task[] arrayTasks1 = new Task[tasks.size()];
		arrayTasks1 = tasks.toArray(arrayTasks1);

		Task[] arrayTasks2 = new Task[expectedTasks.size()];
		arrayTasks2 = expectedTasks.toArray(arrayTasks2);

		assertArrayEquals(arrayTasks1, arrayTasks2);
	}

	@Test
	public void testImportTasks() {
		ToDoList list = new ToDoList();

		final String fileName = "exports";

		boolean actual = list.importTasksFromFile(fileName);
		boolean expected = importTasks(fileName, list);

		assertEquals(expected, actual);

	}

	private boolean importTasks(String fileName, ToDoList list) {

		if (fileName != null) {

			Path path = Paths.get(fileName);
			if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
				return false;
			}

			String line;

			try (BufferedReader reader = Files.newBufferedReader(path)) {

				while ((line = reader.readLine()) != null) {

					list.constructTask(line);
					
				}

				return true;

			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

		}

		return false;
	}

	@Test
	public void testExportTasks(){
		ToDoList list = new ToDoList();
		
		final String fileName = "exports";
		
		boolean actual = list.exportTasksToFile(fileName);
		boolean expected =  exportTasks(fileName, list);
		
		assertEquals(expected, actual);
	}

	private boolean exportTasks(String fileName, ToDoList list) {
		
		if(fileName != null && list != null){
			
			Path path = Paths.get(fileName);
			
			if(!Files.exists(path, LinkOption.NOFOLLOW_LINKS)){
				return false;
			}
			
			list.writeToFile(fileName);
			
		}
		
		return false;
	}

}

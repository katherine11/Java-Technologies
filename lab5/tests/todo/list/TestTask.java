package todo.list;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

public class TestTask {

	@Test
	public void testToString(){
		Task task = new Task("buy cake", TaskStatus.IN_PROCESS, (byte)3 , LocalDate.of(2017, 1, 1));
		String expected = "buy cake,IN_PROCESS,3,2017-01-01";
	
		assertEquals(expected, task.toString());
	}
}

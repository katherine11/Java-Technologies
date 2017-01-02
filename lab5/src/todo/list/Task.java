package todo.list;

import java.time.LocalDate;

public class Task implements Comparable<Task>{
	
	private String name;
	private String description;
	private TaskStatus status;
	private byte priority;
	private LocalDate deadline;
	
//	public Task(){
//		
//	}
	
	public Task(String name,TaskStatus status, byte priority, LocalDate deadline) {
		
		if(name != null){
			this.name = name;
		}
		else{
			System.out.println("Invalid name!");
		}
		
		if(status != null){
			this.status = status;
		}
		
		if(priority > 0){
			this.priority = priority;
		}
		
		if(deadline != null){
			this.deadline = deadline;
		}
	}

	@Override
	public int compareTo(Task o) {
		
		if(this.priority < o.priority){
			return -1;
		}
		
		if(this.priority > o.priority){
			return 1;
		}
		
		return 0;
		
	}
	
	@Override
	public String toString() {
		return name + "," + status + ","+ priority + "," + deadline;
	}
	
	public Task convertToTask(String task){
		
		String [] fields = task.split(",");
		
		return null;
	}
	
	public LocalDate getDeadline() {
		return deadline;
	}
	
	public TaskStatus getStatus() {
		return status;
	}

	public byte getPriority() {
		return priority;
	}
	
}

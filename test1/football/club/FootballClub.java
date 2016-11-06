package football.club;

import java.time.LocalDate;
import java.util.Arrays;

import football.club.exceptions.FootballClubException;

public class FootballClub implements IFootballClub {
	
	private static final int MAX_EMPLOYEES = 60;
	private Employee [] employees = new Employee [MAX_EMPLOYEES];
	private int size;
	
	@Override
	public void addEmployee(Employee employee) throws FootballClubException{
	
		if(employee != null){
			this.employees[size++] = employee;
		}
		else{
			throw new FootballClubException("Invalid employee given!");
		}
		
	}
	
	@Override
	public void printAllEmployeesOrderedByDate() throws FootballClubException{
		
		Employee [] result = this.employees.clone();
		
		Arrays.sort(result);
		
		printEmployees(result);
		
	}
	
	private void printEmployees(Employee[] result) throws FootballClubException {
		
		if(result != null){
			for (int index = 0; index < result.length; index++) {
				System.out.println(employees[index]);
			}
		}
		else{
			throw new FootballClubException("Invalid employees given!");
		}
	}

	@Override
	public void printAllEmployeesWithExpiredContract(LocalDate date) throws FootballClubException{
		Employee [] result = new Employee[this.employees.length];
		int size = 0;
		if(date != null){
			for (int index = 0; index < this.employees.length; index++) {
				if(this.employees[index].getContractExpireDate().isBefore(date)){
					result[size++] = this.employees[index];
				}
			}
			printEmployees(result);
		}
		else{
			throw new FootballClubException("Invalid date given!");
		}
	}
	
	@Override
	public void printSalaryForEachEmployee(){
		for (int index = 0; index < this.employees.length; index++) {
			System.out.println("Employee name: " + this.employees[index].getName());
			System.out.println("Employee salary: " + this.employees[index].salary);
			System.out.println();
		}
	}
		

}

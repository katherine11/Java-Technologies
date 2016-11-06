package football.club;

import java.time.LocalDate;

import football.club.exceptions.EmployeeException;

public class HelpingStaff extends Employee {

	private static final double BONUS_PERCENT = 0.05;
	private static final int HELPING_STAFF_SALARY = 1000;
	private String position;
	private byte experienceInYears;

	public HelpingStaff(String name, byte age, LocalDate contractExpireDate, String position, byte experience) throws EmployeeException {
		super(name, age, contractExpireDate);
		
		if(position == null || experience < 0){
			throw new EmployeeException("Invalid data given!");
		}
		
		this.position = position;
		this.experienceInYears = experience;
	}

	@Override
	void calculateSalary() {
		float bonus = (float) (experienceInYears*this.salary*BONUS_PERCENT);
		this.salary = HELPING_STAFF_SALARY + bonus;
	}
}

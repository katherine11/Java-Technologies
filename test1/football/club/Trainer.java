package football.club;

import java.time.LocalDate;

import football.club.exceptions.EmployeeException;
import football.club.exceptions.FootballClubException;

public class Trainer extends Employee{

	private static final int MIN_TITLES_NUMBER = 5;
	private static final int TRAINER_SALARY = 8000;
	private byte titles;
	private TrainerPosition position;

	public Trainer(String name, byte age, LocalDate contractExpireDate,TrainerPosition position, byte titles) throws EmployeeException{
		super(name, age, contractExpireDate);
		
		if(titles < 0 || position == null){
			throw new EmployeeException("Invalid data given!");
		}
		
		this.titles = titles;
		this.position = position;
	}
	
	@Override
	void calculateSalary() {
		float bonus = 0;
		
		if(this.position.equals(TrainerPosition.SENIOR) && this.titles > MIN_TITLES_NUMBER){
			bonus = 5000;
		}
		
		this.salary = TRAINER_SALARY + bonus;		
	}
	
	
}

package football.club;

import java.time.LocalDate;

import football.club.exceptions.EmployeeException;

public class FootballPlayer extends Employee{

	private static final int BONUS_SALARY = 250;
	private static final int FOOTBALL_PLAYER_SALARY = 10000;
	private FootballerPosition position;
	private byte games;

	public FootballPlayer(String name, byte age, LocalDate contractExpireDate, FootballerPosition position, byte games) throws EmployeeException {
		super(name, age, contractExpireDate);
		
		if(position == null || games < 0){
			throw new EmployeeException("Invalid position given!");
		}
		
		this.position = position;
		this.games = games;
		
	}

	@Override
	void calculateSalary() {
		float bonus = games*BONUS_SALARY;
		this.salary = FOOTBALL_PLAYER_SALARY + bonus;
	} 
	
}

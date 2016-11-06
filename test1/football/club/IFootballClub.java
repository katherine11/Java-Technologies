package football.club;

import java.time.LocalDate;

import football.club.exceptions.FootballClubException;

public interface IFootballClub {
	
	void addEmployee(Employee employee) throws FootballClubException;
	void printAllEmployeesOrderedByDate() throws FootballClubException;
	void printAllEmployeesWithExpiredContract(LocalDate date) throws FootballClubException;
	void printSalaryForEachEmployee();
}
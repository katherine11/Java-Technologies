package football.club;
import java.time.LocalDate;

import football.club.exceptions.EmployeeException;

public abstract class Employee implements Comparable<Employee> {

	private String name;
	private byte age;
	private LocalDate contractExpireDate;
	protected float salary;

	public Employee(String name, byte age, LocalDate contractExpireDate) throws EmployeeException {
		if(name == null || age < 0 || contractExpireDate == null){
			throw new EmployeeException("Invalid data given!");
		}
		
		this.name = name;
		this.age = age;
		this.contractExpireDate = contractExpireDate;
	}
	
	abstract void calculateSalary();

	@Override
	public int compareTo(Employee employee) {
		if (employee != null) {
			if (this.contractExpireDate.equals(employee.contractExpireDate)) {
				return this.name.compareTo(employee.name);
			}
			else{
				if(this.contractExpireDate.isBefore(employee.contractExpireDate)){
					return 1;
				}
				else{
					return -1;
				}
			}
		}
		return 0;
	}

	@Override
	public String toString() {
		return "Employee [name=" + name + ", age=" + age + ", contractExpireDate=" + contractExpireDate + ", salary="
				+ salary + "]";
	}
	
	public LocalDate getContractExpireDate() {
		return contractExpireDate;
	}

	public String getName() {
		return this.name;
	}

}

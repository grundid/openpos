package org.openpos.timerecording;

public class Employee {

	private String name;
	private double wageRate;
	private String pin;

	public Employee(String name, double wageRate) {
		this.name = name;
		this.wageRate = wageRate;
	}

	public Employee(String name, double wageRate, String pin) {
		this.name = name;
		this.wageRate = wageRate;
		this.pin = pin;
	}

	public String getName() {
		return name;
	}

	public double getWageRate() {
		return wageRate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setWageRate(double wageRate) {
		this.wageRate = wageRate;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

}

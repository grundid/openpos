package org.openpos.timerecording;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;

public class EmployeeDataBase {

	private List<Employee> employees = new ArrayList<Employee>();

	public List<Employee> getEmployees() {
		return employees;
	}

	public void addEmployee(String name, double wageRate, String pin) {
		employees.add(new Employee(name, wageRate, pin));
	}

	@Deprecated
	public void addEmployee(String name, double wageRate) {
		employees.add(new Employee(name, wageRate, ""));
	}

	public void addOrUpdateEmployee(Employee employee) {
		boolean found = false;

		for (Employee e : employees) {
			if (e.getName().equals(employee.getName())) {
				e.setWageRate(employee.getWageRate());
				found = true;
			}
		}
		if (!found)
			employees.add(employee);

	}

	public void removeEmployee(Employee employeeToRemove) {
		for (Iterator<Employee> it = employees.iterator(); it.hasNext();) {
			Employee employee = it.next();
			if (employee.getName().equals(employeeToRemove.getName()))
				it.remove();
		}
	}

	public static EmployeeDataBase getInstance(String script) {
		try {
			EmployeeDataBase employeeDataBase = new EmployeeDataBase();
			ScriptEngine eng = ScriptFactory.getScriptEngine(ScriptFactory.BEANSHELL);
			eng.put("edb", employeeDataBase);
			eng.eval(script);
			return employeeDataBase;
		}
		catch (ScriptException e) {
			throw new RuntimeException(e);
		}
	}

	public String toScript() {
		StringBuilder sb = new StringBuilder();
		for (Employee employee : employees) {
			sb.append("edb.addEmployee(\"").append(employee.getName()).append("\",").append(employee.getWageRate())
					.append(",\"").append(employee.getPin()).append("\");\n");
		}
		return sb.toString();
	}
}

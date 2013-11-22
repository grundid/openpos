package org.openpos.timerecording;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

public class EmployeeDataBaseTest {

	@Test
	public void testGetInstance() throws Exception {
		String script = "edb.addEmployee(\"Test\",35.5);";

		EmployeeDataBase employeeDataBase = EmployeeDataBase.getInstance(script);
		Collection<Employee> employees = employeeDataBase.getEmployees();
		assertFalse(employees.isEmpty());
		Employee employee = employees.iterator().next();
		assertEquals("Test", employee.getName());
		assertEquals(35.5, employee.getWageRate(), 0.001);
	}
}

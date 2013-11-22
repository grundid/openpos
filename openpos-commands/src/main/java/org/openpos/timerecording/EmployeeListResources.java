package org.openpos.timerecording;

import java.io.UnsupportedEncodingException;

import org.openpos.utils.ResourcesHandler;

import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryException;

public class EmployeeListResources extends ResourcesHandler {

	private EmployeeDataBase employeeDataBase;

	public EmployeeListResources(AppView app) {
		super(app);
		employeeDataBase = getUpdatedEmployeeDataBase();
	}

	public EmployeeDataBase getEmployeeDataBase() {
		return employeeDataBase;
	}

	public EmployeeDataBase getUpdatedEmployeeDataBase() {
		String employeeList = dataLogicSystem.getResourceAsText("Employee.List");
		if (employeeList != null) {
			return EmployeeDataBase.getInstance(employeeList);
		}
		else
			throw new BeanFactoryException("Resource not defined");
	}

	public void persistEmployeeDataBase() {
		String data = employeeDataBase.toScript();
		try {
			dataLogicSystem.setResource("Employee.List", 0, data.getBytes("UTF8"));
		}
		catch (UnsupportedEncodingException e) {
		}
	}
}

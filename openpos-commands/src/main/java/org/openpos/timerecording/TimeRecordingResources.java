package org.openpos.timerecording;

import org.openpos.utils.ResourcesHandler;

import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.printer.TicketParser;

public class TimeRecordingResources extends ResourcesHandler {

	private TicketParser ticketParser;
	private String employeeDailyReport;
	private String employeeMonthlyReport;

	public TimeRecordingResources(AppView app) {
		super(app);
		ticketParser = new TicketParser(app.getDeviceTicket(), dataLogicSystem);

		employeeDailyReport = dataLogicSystem.getResourceAsText("Employee.Daily.Report");
		if (employeeDailyReport == null || employeeDailyReport.isEmpty()) {
			throw new BeanFactoryException("Resource not defined");
		}
		employeeMonthlyReport = dataLogicSystem.getResourceAsText("Employee.Monthly.Report");
		if (employeeMonthlyReport == null || employeeMonthlyReport.isEmpty()) {
			throw new BeanFactoryException("Resource not defined");
		}
	}

	public TicketParser getTicketParser() {
		return ticketParser;
	}

	public String getEmployeeDailyReport() {
		return employeeDailyReport;
	}

	public String getEmployeeMonthlyReport() {
		return employeeMonthlyReport;
	}

}

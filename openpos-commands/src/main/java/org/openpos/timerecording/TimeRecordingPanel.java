package org.openpos.timerecording;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.openpos.print.ReportPrintService;

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.JPanelView;

public class TimeRecordingPanel implements JPanelView, BeanFactoryApp {

	private TimeRecordingView timeRecordingView = new TimeRecordingView();
	private EmployeeListResources employeeListResources;

	@Override
	public Object getBean() {
		return this;
	}

	@Override
	public void init(AppView app) throws BeanFactoryException {
		TimeRecordingResources resources = new TimeRecordingResources(app);
		employeeListResources = new EmployeeListResources(app);
		timeRecordingView.setEmployees(employeeListResources.getEmployeeDataBase().getEmployees());
		timeRecordingView.setReportPrintService(new ReportPrintService(resources.getTicketParser(), timeRecordingView,
				resources.getEmployeeDailyReport()));
	}

	@Override
	public String getTitle() {
		return "Zeiterfassung";
	}

	@Override
	public void activate() throws BasicException {
		timeRecordingView.setEmployees(employeeListResources.getUpdatedEmployeeDataBase().getEmployees());
		timeRecordingView.resetFields();
	}

	@Override
	public boolean deactivate() {
		return true;
	}

	@Override
	public JComponent getComponent() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(timeRecordingView);
		return panel;
	}
}

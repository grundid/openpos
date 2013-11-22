package org.openpos.timerecording.report;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.openpos.print.ReportPrintService;
import org.openpos.timerecording.EmployeeListResources;
import org.openpos.timerecording.TimeRecordingResources;

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.JPanelView;

public class TimeRecordingReportPanel implements JPanelView, BeanFactoryApp {

	private TimeRecordingReportView timeRecordingReportView = new TimeRecordingReportView();

	@Override
	public Object getBean() {
		return this;
	}

	@Override
	public void init(AppView app) throws BeanFactoryException {
		TimeRecordingResources resources = new TimeRecordingResources(app);
		EmployeeListResources employeeListResources = new EmployeeListResources(app);
		timeRecordingReportView.setEmployees(employeeListResources.getEmployeeDataBase().getEmployees());
		timeRecordingReportView.setReportPrintService(new ReportPrintService(resources.getTicketParser(),
				timeRecordingReportView, resources.getEmployeeMonthlyReport()));
	}

	@Override
	public String getTitle() {
		return "Lohnabrechnung";
	}

	@Override
	public void activate() throws BasicException {
	}

	@Override
	public boolean deactivate() {
		return true;
	}

	@Override
	public JComponent getComponent() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(timeRecordingReportView);
		return panel;
	}

}

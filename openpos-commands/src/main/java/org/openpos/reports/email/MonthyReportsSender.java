package org.openpos.reports.email;

import java.util.ArrayList;
import java.util.List;

import org.openpos.reports.IReportsPublishService;
import org.openpos.reports.closemonth.JPanelCloseMonth;
import org.openpos.reports.closemonth.PaymentsModelLoader;
import org.openpos.timerecording.Employee;
import org.openpos.timerecording.EmployeeListResources;
import org.openpos.timerecording.TimeRecordingResources;
import org.openpos.timerecording.dao.MonthlyReportingInsert;
import org.openpos.timerecording.report.TimeRecordingReportCreator;
import org.openpos.timerecording.report.TimeRecordingReportModel;
import org.openpos.utils.LegacyFactory;

import com.openbravo.pos.panels.PaymentsModel;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;

public class MonthyReportsSender implements Runnable {

	private int month;
	private int year;
	private LegacyFactory legacyFactory;
	private IReportsPublishService reportsPublishService;
	private MonthlyReportingInsert monthlyReportingInsert;

	public MonthyReportsSender(int month, int year, LegacyFactory legacyFactory,
			IReportsPublishService reportsPublishService, MonthlyReportingInsert monthlyReportingInsert) {
		this.month = month;
		this.year = year;
		this.legacyFactory = legacyFactory;
		this.reportsPublishService = reportsPublishService;
		this.monthlyReportingInsert = monthlyReportingInsert;
	}

	@Override
	public void run() {
		monthlyCash();
		monthlyTimeReporting();
		MonthlyReporting monthlyReporting = new MonthlyReporting(month, year, "OK");
		monthlyReportingInsert.insert(monthlyReporting);
	}

	private void monthlyTimeReporting() {
		try {
			TimeRecordingResources resources = new TimeRecordingResources(legacyFactory.getAppView());
			EmployeeListResources employeeListResources = new EmployeeListResources(legacyFactory.getAppView());
			List<Employee> employees = employeeListResources.getEmployeeDataBase().getEmployees();
			TimeRecordingReportCreator creator = new TimeRecordingReportCreator(employees);
			List<String> evaluatedReports = new ArrayList<String>();
			List<TimeRecordingReportModel> reports = creator.createReports(month, year);
			for (TimeRecordingReportModel reportModel : reports) {
				String evaluatedReport = evalScriptResource(resources.getEmployeeMonthlyReport(), "report", reportModel);
				evaluatedReports.add(evaluatedReport);
			}
			reportsPublishService.sendMonthlyTimeReport(month, year, evaluatedReports);
		}
		catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	private void monthlyCash() {
		try {
			PaymentsModel paymentsToClose = new PaymentsModelLoader(legacyFactory.getAppView().getSession(), month,
					year).getPaymentsModel();
			String evaluatedReport = evalScript(JPanelCloseMonth.PRINTER_MONTHLY_REPORT, "payments", paymentsToClose);
			reportsPublishService.sendMonthlyCashReport(month, year, evaluatedReport);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String evalScript(String scriptName, String key, Object value) throws ScriptException {
		String sresource = legacyFactory.getDataLogicSystem().getResourceAsXML(scriptName);
		return evalScriptResource(sresource, key, value);
	}

	private String evalScriptResource(String sresource, String key, Object value) throws ScriptException {
		ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
		script.put(key, value);
		return script.eval(sresource).toString();
	}
}

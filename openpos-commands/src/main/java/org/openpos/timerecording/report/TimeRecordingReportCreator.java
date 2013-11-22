package org.openpos.timerecording.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openpos.OpenPos;
import org.openpos.timerecording.Employee;
import org.openpos.timerecording.FormattedTimeRecordingModel;
import org.openpos.timerecording.TimeRecordingModel;
import org.openpos.timerecording.TimeRecordingModelFormatter;
import org.openpos.timerecording.dao.TimeRecordingSelect;
import org.openpos.utils.DateTimeUtils;

public class TimeRecordingReportCreator {

	private List<Employee> employees;

	public TimeRecordingReportCreator(List<Employee> employees) {
		this.employees = employees;
	}

	public List<TimeRecordingReportModel> createReports(int month, int year) {
		List<TimeRecordingReportModel> result = new ArrayList<TimeRecordingReportModel>();
		Calendar fromDate = Calendar.getInstance();
		fromDate.set(year, month, 1);
		Calendar toDate = (Calendar)fromDate.clone();
		toDate.add(Calendar.MONTH, 1);
		toDate.add(Calendar.DAY_OF_YEAR, -1);
		TimeRecordingModelFormatter formatter = new TimeRecordingModelFormatter();
		Map<String, List<TimeRecordingModel>> data = collectData(fromDate, toDate);
		for (Entry<String, List<TimeRecordingModel>> entry : data.entrySet()) {
			TimeRecordingReportModel reportModel = new TimeRecordingReportModel();
			reportModel.setEmployeeName(entry.getKey());
			reportModel.setFromDate(formatter.formatCalendar(fromDate));
			reportModel.setToDate(formatter.formatCalendar(toDate));
			List<FormattedTimeRecordingModel> entries = new ArrayList<FormattedTimeRecordingModel>();
			int totalWorkingTime = 0;
			int totalEarningsInCent = 0;
			for (TimeRecordingModel model : entry.getValue()) {
				entries.add(formatter.formatModel(model));
				totalWorkingTime += model.getWorkingTime();
				totalEarningsInCent += model.getEarningsInCents();
			}
			reportModel.setEntries(entries);
			reportModel.setTotalWorkingTime(DateTimeUtils.formatTime(totalWorkingTime));
			reportModel.setTotalEarnings(formatter.formatEarningsInCents(totalEarningsInCent));
			result.add(reportModel);
		}
		return result;
	}

	private Map<String, List<TimeRecordingModel>> collectData(Calendar fromDate, Calendar toDate) {
		Map<String, List<TimeRecordingModel>> result = new LinkedHashMap<String, List<TimeRecordingModel>>();
		TimeRecordingSelect timeRecordingSelect = OpenPos.getApplicationContext().getBean(TimeRecordingSelect.class);
		for (Employee employee : employees) {
			List<TimeRecordingModel> list = timeRecordingSelect.findTimeRecording(employee.getName(),
					fromDate.getTime(), toDate.getTime());
			if (!list.isEmpty()) {
				result.put(employee.getName(), list);
			}
		}
		return result;
	}
}

package org.openpos.timerecording.report;

import java.util.Collection;

import org.openpos.timerecording.FormattedTimeRecordingModel;

public class TimeRecordingReportModel {

	private String employeeName;
	private String fromDate;
	private String toDate;
	private String totalWorkingTime;
	private String totalEarnings;

	private Collection<FormattedTimeRecordingModel> entries;

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Collection<FormattedTimeRecordingModel> getEntries() {
		return entries;
	}

	public void setEntries(Collection<FormattedTimeRecordingModel> entries) {
		this.entries = entries;
	}

	public String getTotalWorkingTime() {
		return totalWorkingTime;
	}

	public void setTotalWorkingTime(String totalWorkingTime) {
		this.totalWorkingTime = totalWorkingTime;
	}

	public String getTotalEarnings() {
		return totalEarnings;
	}

	public void setTotalEarnings(String totalEarnings) {
		this.totalEarnings = totalEarnings;
	}

}

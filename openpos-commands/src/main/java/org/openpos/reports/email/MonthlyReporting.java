package org.openpos.reports.email;

public class MonthlyReporting {

	private int monthlyReportingId;
	private int month;
	private int year;
	private String status;

	public MonthlyReporting() {
	}

	public MonthlyReporting(int month, int year, String status) {
		this.month = month;
		this.year = year;
		this.status = status;
	}

	public int getMonthlyReportingId() {
		return monthlyReportingId;
	}

	public void setMonthlyReportingId(int monthlyReportingId) {
		this.monthlyReportingId = monthlyReportingId;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

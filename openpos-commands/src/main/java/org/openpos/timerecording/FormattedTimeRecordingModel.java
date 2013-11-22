package org.openpos.timerecording;

public class FormattedTimeRecordingModel {

	private String employeeName;
	private String date;
	private String commingTime;
	private String leavingTime;
	private String pauseDuration;
	private String workingTime;
	private String earnings;

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCommingTime() {
		return commingTime;
	}

	public void setCommingTime(String commingTime) {
		this.commingTime = commingTime;
	}

	public String getLeavingTime() {
		return leavingTime;
	}

	public void setLeavingTime(String leavingTime) {
		this.leavingTime = leavingTime;
	}

	public String getPauseDuration() {
		return pauseDuration;
	}

	public void setPauseDuration(String pauseDuration) {
		this.pauseDuration = pauseDuration;
	}

	public String getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(String workingTime) {
		this.workingTime = workingTime;
	}

	public String getEarnings() {
		return earnings;
	}

	public void setEarnings(String earnings) {
		this.earnings = earnings;
	}

}

package org.openpos.reports;

import java.util.Calendar;
import java.util.Date;

public class CashFromToModel {

	private int fromTime;
	private int toTime;
	private Calendar date = Calendar.getInstance();

	public int getFromTime() {
		return fromTime;
	}

	public void setFromTime(int fromTime) {
		this.fromTime = fromTime;
	}

	public int getToTime() {
		return toTime;
	}

	public void setToTime(int toTime) {
		this.toTime = toTime;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public Date getFromDate() {
		Calendar c = (Calendar)date.clone();
		resetTime(c);
		c.add(Calendar.MINUTE, fromTime);
		return c.getTime();
	}

	public Date getToDate() {
		Calendar c = (Calendar)date.clone();
		resetTime(c);
		c.add(Calendar.MINUTE, toTime);
		return c.getTime();
	}

	private void resetTime(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
	}
}

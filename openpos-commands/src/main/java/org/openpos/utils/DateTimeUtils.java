package org.openpos.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeUtils {

	public static String[] createYearsArray(int startingYear) {
		final String[] years = new String[Calendar.getInstance().get(Calendar.YEAR) - startingYear + 1];
		for (int y = 0; y < years.length; y++) {
			years[y] = String.valueOf(startingYear + y);
		}
		return years;
	}

	public static String[] createMonthArray() {
		String[] months = new String[12];
		SimpleDateFormat sdf = new SimpleDateFormat("MMMMM");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		for (int month = 0; month < months.length; month++) {
			c.set(Calendar.MONTH, month);
			months[month] = sdf.format(c.getTime());
		}
		return months;
	}

	public static String[] createDayArray() {
		String[] days = new String[31];
		for (int x = 0; x < days.length; x++) {
			days[x] = String.valueOf(x + 1);
		}
		return days;
	}

	public static String formatTime(int hour, int minute) {
		DecimalFormat zeroDf = new DecimalFormat("00");
		return zeroDf.format(hour) + ":" + zeroDf.format(minute);
	}

	public static String formatTime(int time) {
		return formatTime(time / 60, time % 60);
	}

	public static int calcWorkingTime(int commingTime, int leavingTime, int pauseDuration) {
		int workingTime = 0;

		if (leavingTime - commingTime >= 0) {
			workingTime += leavingTime - commingTime;
		}
		else {
			workingTime += (24 * 60 - commingTime) + leavingTime;
		}

		return workingTime - pauseDuration;
	}

}

package org.openpos.utils;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {

	public static Calendar fromDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
}

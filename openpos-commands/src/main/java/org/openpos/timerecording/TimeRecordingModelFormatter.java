package org.openpos.timerecording;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;

import org.openpos.utils.DateTimeUtils;

public class TimeRecordingModelFormatter {

	private DateFormat sdf = DateFormat.getDateInstance(DateFormat.MEDIUM);
	private DecimalFormat df = new DecimalFormat("#,##0.00");

	public FormattedTimeRecordingModel formatModel(TimeRecordingModel timeRecordingModel) {
		FormattedTimeRecordingModel model = new FormattedTimeRecordingModel();
		model.setEmployeeName(timeRecordingModel.getEmployeeName());
		model.setCommingTime(DateTimeUtils.formatTime(timeRecordingModel.getCommingTime()));
		model.setLeavingTime(DateTimeUtils.formatTime(timeRecordingModel.getLeavingTime()));
		model.setPauseDuration(DateTimeUtils.formatTime(timeRecordingModel.getPauseDuration()));
		model.setWorkingTime(DateTimeUtils.formatTime(timeRecordingModel.getWorkingTime()));
		model.setDate(sdf.format(timeRecordingModel.getDate().getTime()));
		model.setEarnings(formatEarningsInCents(timeRecordingModel.getEarningsInCents()));
		return model;
	}

	public String formatCalendar(Calendar calendar) {
		return sdf.format(calendar.getTime());
	}

	public String formatEarningsInCents(int earningsInCents) {
		return df.format((double)earningsInCents / 100.0);
	}

}

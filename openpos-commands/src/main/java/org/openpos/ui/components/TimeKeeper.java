package org.openpos.ui.components;

public class TimeKeeper {

	private static final int MIN_HOUR = 0;
	private static final int MAX_HOUR = 24;
	private static final int MIN_MINUTE = 0;
	private static final int MAX_MINUTE = 60;

	private int minute = 0;
	private int hour = 0;
	private int hourDelta = 1;
	private int minuteDelta = 5;

	public void incMinute() {
		setMinute(minute + minuteDelta);
	}

	public void decMinute() {
		if (minute - minuteDelta >= MIN_MINUTE)
			setMinute(minute - minuteDelta);
		else
			setMinute(MAX_MINUTE - minuteDelta);
	}

	public void incHour() {
		setHour(hour + hourDelta);
	}

	public void decHour() {
		if (hour - hourDelta >= MIN_HOUR)
			setHour(hour - hourDelta);
		else
			setHour(MAX_HOUR - hourDelta);
	}

	public int getTime() {
		return hour * 60 + minute;
	}

	public void setTime(int time) {
		setHour(time / 60);
		setMinute(time % 60);
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = ((minute % MAX_MINUTE) / minuteDelta) * minuteDelta;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = ((hour % MAX_HOUR) / hourDelta) * hourDelta;
	}

}

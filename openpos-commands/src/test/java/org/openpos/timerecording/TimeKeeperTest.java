package org.openpos.timerecording;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openpos.ui.components.TimeKeeper;

public class TimeKeeperTest {

	private TimeKeeper timeKeeper;

	@Before
	public void setUp() {
		timeKeeper = new TimeKeeper();
	}

	@Test
	public void testSetTime() throws Exception {

		timeKeeper.setTime(0);
		assertEqualsHourMinute(0, 0);
		timeKeeper.setTime(60);
		assertEqualsHourMinute(1, 0);
		timeKeeper.setTime(120);
		assertEqualsHourMinute(2, 0);
		timeKeeper.setTime(90);
		assertEqualsHourMinute(1, 30);
		timeKeeper.setTime(119);
		assertEqualsHourMinute(1, 55);
		timeKeeper.setTime(23 * 60 + 59);
		assertEqualsHourMinute(23, 55);
	}

	@Test
	public void testSetMinute() throws Exception {
		timeKeeper.setMinute(0);
		assertEqualsHourMinute(0, 0);
		timeKeeper.setMinute(4);
		assertEqualsHourMinute(0, 0);
		timeKeeper.setMinute(30);
		assertEqualsHourMinute(0, 30);
		timeKeeper.setMinute(90);
		assertEqualsHourMinute(0, 30);
	}

	@Test
	public void testDecMinute() throws Exception {
		timeKeeper.decMinute();
		assertEqualsHourMinute(0, 55);
		timeKeeper.decMinute();
		assertEqualsHourMinute(0, 50);
	}

	@Test
	public void testIncMinute() throws Exception {
		timeKeeper.setTime(50);
		assertEqualsHourMinute(0, 50);
		timeKeeper.incMinute();
		assertEqualsHourMinute(0, 55);
		timeKeeper.incMinute();
		assertEqualsHourMinute(0, 0);
	}

	@Test
	public void testDecHour() throws Exception {
		timeKeeper.decHour();
		assertEqualsHourMinute(23, 0);
		timeKeeper.decHour();
		assertEqualsHourMinute(22, 0);
	}

	@Test
	public void testIncHour() throws Exception {
		timeKeeper.setTime(22 * 60);
		assertEqualsHourMinute(22, 0);
		timeKeeper.incHour();
		assertEqualsHourMinute(23, 0);
		timeKeeper.incHour();
		assertEqualsHourMinute(0, 0);
	}

	private void assertEqualsHourMinute(int hour, int minute) {
		assertEquals(hour, timeKeeper.getHour());
		assertEquals(minute, timeKeeper.getMinute());
	}
}

package org.openpos.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class DateTimeUtilsTest {

	@Test
	public void testCalcWorkingTime() throws Exception {

		assertEquals(0, DateTimeUtils.calcWorkingTime(0, 0, 0));
		assertEquals(60, DateTimeUtils.calcWorkingTime(0, 60, 0));
		assertEquals(120, DateTimeUtils.calcWorkingTime(60, 180, 0));
		assertEquals(120, DateTimeUtils.calcWorkingTime(60, 240, 60));
		assertEquals(120, DateTimeUtils.calcWorkingTime(23 * 60, 60, 0));
		assertEquals(240, DateTimeUtils.calcWorkingTime(23 * 60, 240, 60));
	}
}

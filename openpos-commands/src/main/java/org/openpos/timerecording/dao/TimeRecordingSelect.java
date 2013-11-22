package org.openpos.timerecording.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.openpos.timerecording.TimeRecordingModel;
import org.openpos.utils.CalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.stereotype.Repository;

@Repository
public class TimeRecordingSelect extends MappingSqlQuery<TimeRecordingModel> {

	@Autowired
	public TimeRecordingSelect(DataSource dataSource) {
		setDataSource(dataSource);
		setSql("SELECT EMPLOYEE_NAME, WORK_DATE, COMMING_TIME, "
				+ "LEAVING_TIME, PAUSE_DURATION, WORKING_TIME, EARNINGS FROM TIMERECORDING "
				+ "WHERE EMPLOYEE_NAME = ? AND WORK_DATE >= ? AND WORK_DATE <= ? "
				+ "ORDER BY WORK_DATE ASC, COMMING_TIME ASC");
		setTypes(new int[] { Types.VARCHAR, Types.DATE, Types.DATE });
	}

	@Override
	protected TimeRecordingModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		int c = 1;
		TimeRecordingModel model = new TimeRecordingModel();
		model.setEmployeeName(rs.getString(c++));
		model.setDate(CalendarUtils.fromDate(rs.getDate(c++)));
		model.setCommingTime(rs.getInt(c++));
		model.setLeavingTime(rs.getInt(c++));
		model.setPauseDuration(rs.getInt(c++));
		model.setWorkingTime(rs.getInt(c++));
		model.setEarningsInCents(rs.getInt(c++));
		return model;
	}

	public List<TimeRecordingModel> findTimeRecording(String employeeName, Date fromDate, Date toDate) {
		return execute(employeeName, fromDate, toDate);
	}
}

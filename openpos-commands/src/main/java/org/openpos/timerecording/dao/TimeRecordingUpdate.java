package org.openpos.timerecording.dao;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import org.openpos.timerecording.TimeRecordingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.stereotype.Repository;

@Repository
public class TimeRecordingUpdate extends SqlUpdate {

	@Autowired
	public TimeRecordingUpdate(DataSource dataSource) {
		setDataSource(dataSource);
		setSql("UPDATE TIMERECORDING SET COMMING_TIME=?, "
				+ "LEAVING_TIME=?, PAUSE_DURATION=?, WORKING_TIME=?, EARNINGS=?, CREATED=? "
				+ "WHERE EMPLOYEE_NAME=? AND WORK_DATE=?");
		setTypes(new int[] { Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER,
				Types.TIMESTAMP, Types.VARCHAR, Types.DATE });
	}

	public void update(TimeRecordingModel model) {
		update(model.getCommingTime(), model.getLeavingTime(), model.getPauseDuration(), model.getWorkingTime(),
				model.getEarningsInCents(), new Timestamp(Calendar.getInstance().getTimeInMillis()),
				model.getEmployeeName(), new Date(model.getDate().getTimeInMillis()));
	}
}

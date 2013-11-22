package org.openpos.timerecording.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;

import javax.sql.DataSource;

import org.openpos.timerecording.TimeRecordingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.stereotype.Repository;

@Repository
public class TimeRecordingInsert extends SqlUpdate {

	@Autowired
	public TimeRecordingInsert(DataSource dataSource) {
		setDataSource(dataSource);
		setSql("INSERT INTO TIMERECORDING (EMPLOYEE_NAME, WORK_DATE, COMMING_TIME, "
				+ "LEAVING_TIME, PAUSE_DURATION, WORKING_TIME, EARNINGS, CREATED) VALUES (?,?,?,?,?,?,?,?)");
		setTypes(new int[] { Types.VARCHAR, Types.DATE, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER,
				Types.INTEGER, Types.TIMESTAMP });
	}

	public void insert(TimeRecordingModel model) {
		update(model.getEmployeeName(), new Date(model.getDate().getTimeInMillis()), model.getCommingTime(),
				model.getLeavingTime(), model.getPauseDuration(), model.getWorkingTime(), model.getEarningsInCents(),
				new Timestamp(Calendar.getInstance().getTimeInMillis()));
	}
}

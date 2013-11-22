package org.openpos.timerecording.dao;

import java.sql.Types;

import javax.sql.DataSource;

import org.openpos.reports.email.MonthlyReporting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.stereotype.Repository;

@Repository
public class MonthlyReportingInsert extends SqlUpdate {

	@Autowired
	public MonthlyReportingInsert(DataSource dataSource) {
		setDataSource(dataSource);
		setSql("INSERT INTO MONTHLY_REPORTING (MONTH, YEAR, STATUS) VALUES (?,?,?)");
		setTypes(new int[] { Types.INTEGER, Types.INTEGER, Types.VARCHAR });
	}

	public void insert(MonthlyReporting model) {
		update(model.getMonth(), model.getYear(), model.getStatus());
	}
}

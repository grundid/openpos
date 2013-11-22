package org.openpos.timerecording.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.openpos.reports.email.MonthlyReporting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.stereotype.Repository;

@Repository
public class MonthlyReportingSelect extends MappingSqlQuery<MonthlyReporting> {

	@Autowired
	public MonthlyReportingSelect(DataSource dataSource) {
		setDataSource(dataSource);
		setSql("SELECT MONTHLY_REPORTING_ID, MONTH, YEAR, STATUS "
				+ "FROM MONTHLY_REPORTING WHERE MONTH = ? AND YEAR = ?");
		setTypes(new int[] { Types.INTEGER, Types.INTEGER });
	}

	@Override
	protected MonthlyReporting mapRow(ResultSet rs, int rowNum) throws SQLException {
		int c = 1;
		MonthlyReporting model = new MonthlyReporting();
		model.setMonthlyReportingId(rs.getInt(c++));
		model.setMonth(rs.getInt(c++));
		model.setYear(rs.getInt(c++));
		model.setStatus(rs.getString(c++));
		return model;
	}

	public MonthlyReporting findReporting(int month, int year) {
		return findObject(month, year);
	}
}

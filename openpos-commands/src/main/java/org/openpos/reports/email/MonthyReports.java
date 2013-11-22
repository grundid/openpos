package org.openpos.reports.email;

import java.util.Calendar;

import org.openpos.reports.IReportsPublishService;
import org.openpos.timerecording.dao.MonthlyReportingInsert;
import org.openpos.timerecording.dao.MonthlyReportingSelect;
import org.openpos.utils.LegacyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonthyReports {

	@Autowired
	private LegacyFactory legacyFactory;
	@Autowired
	private IReportsPublishService reportsPublishService;
	@Autowired
	private MonthlyReportingSelect monthlyReportingSelect;
	@Autowired
	private MonthlyReportingInsert monthlyReportingInsert;

	public void startReporting() {
		Calendar lastMonth = Calendar.getInstance();
		lastMonth.set(Calendar.DAY_OF_MONTH, 1);
		lastMonth.add(Calendar.DAY_OF_MONTH, -1);
		int month = lastMonth.get(Calendar.MONTH);
		int year = lastMonth.get(Calendar.YEAR);
		MonthlyReporting monthlyReporting = monthlyReportingSelect.findReporting(month, year);
		if (monthlyReporting == null) {
			MonthyReportsSender sender = new MonthyReportsSender(month, year, legacyFactory, reportsPublishService,
					monthlyReportingInsert);
			Thread thread = new Thread(sender);
			thread.start();
		}
	}
}

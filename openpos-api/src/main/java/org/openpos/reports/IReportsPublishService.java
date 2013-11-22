package org.openpos.reports;

import java.util.List;

public interface IReportsPublishService {

	void sendDayEndReport(String evaluatedReport);

	void sendMonthlyCashReport(int month, int year, String evaluatedReport);

	void sendMonthlyTimeReport(int month, int year, List<String> evaluatedReports);
}
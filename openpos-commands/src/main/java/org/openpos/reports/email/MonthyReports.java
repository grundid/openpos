package org.openpos.reports.email;

import org.openpos.reports.IReportsPublishService;
import org.openpos.timerecording.dao.MonthlyReportingInsert;
import org.openpos.timerecording.dao.MonthlyReportingSelect;
import org.openpos.utils.LegacyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class MonthyReports {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private LegacyFactory legacyFactory;
    @Autowired
    private IReportsPublishService reportsPublishService;
    @Autowired
    private MonthlyReportingSelect monthlyReportingSelect;
    @Autowired
    private MonthlyReportingInsert monthlyReportingInsert;

    public void startReporting() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM YYYY");
        Calendar lastMonth = Calendar.getInstance();
        lastMonth.set(Calendar.DAY_OF_MONTH, 1);
        lastMonth.add(Calendar.DAY_OF_MONTH, -1);
        lastMonth.set(Calendar.DAY_OF_MONTH, 1); // set first of month again
        for (int x = 0; x < 12; x++) {
            int month = lastMonth.get(Calendar.MONTH);
            int year = lastMonth.get(Calendar.YEAR);
            MonthlyReporting monthlyReporting = monthlyReportingSelect.findReporting(month, year);
            if (monthlyReporting == null) {
                log.info("Monatsreport für {}", dateFormat.format(lastMonth.getTime()));
                MonthyReportsSender sender = new MonthyReportsSender(month, year, legacyFactory, reportsPublishService,
                        monthlyReportingInsert);
                Thread thread = new Thread(sender);
                thread.start();
            }
            lastMonth.add(Calendar.MONTH, -1);
        }
    }
}

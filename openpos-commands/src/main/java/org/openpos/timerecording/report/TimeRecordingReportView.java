package org.openpos.timerecording.report;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.openpos.print.ReportPrintService;
import org.openpos.timerecording.Employee;
import org.openpos.utils.DateTimeUtils;

import com.openbravo.data.gui.MessageInf;

public class TimeRecordingReportView extends JPanel {

	private String[] years = DateTimeUtils.createYearsArray(2011);
	private ReportPrintService reportPrintService;
	private List<Employee> employees;
	private JComboBox yearSelector;
	private JComboBox monthSelector;

	public TimeRecordingReportView() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 100, 150, 100, 0 };
		gridBagLayout.rowHeights = new int[] { 50, 50, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		JLabel lblMonth = new JLabel("Monat:");
		lblMonth.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_lblMonth = new GridBagConstraints();
		gbc_lblMonth.fill = GridBagConstraints.BOTH;
		gbc_lblMonth.insets = new Insets(0, 0, 5, 5);
		gbc_lblMonth.gridx = 0;
		gbc_lblMonth.gridy = 0;
		add(lblMonth, gbc_lblMonth);
		monthSelector = new JComboBox(DateTimeUtils.createMonthArray());
		monthSelector.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_monthSelector = new GridBagConstraints();
		gbc_monthSelector.insets = new Insets(0, 0, 5, 5);
		gbc_monthSelector.fill = GridBagConstraints.BOTH;
		gbc_monthSelector.gridx = 1;
		gbc_monthSelector.gridy = 0;
		add(monthSelector, gbc_monthSelector);
		yearSelector = new JComboBox(years);
		yearSelector.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_yearSelector = new GridBagConstraints();
		gbc_yearSelector.insets = new Insets(0, 0, 5, 0);
		gbc_yearSelector.fill = GridBagConstraints.BOTH;
		gbc_yearSelector.gridx = 2;
		gbc_yearSelector.gridy = 0;
		add(yearSelector, gbc_yearSelector);
		JButton btnPrint = new JButton("Drucken");
		btnPrint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				printReport();
			}
		});
		btnPrint.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_btnPrint = new GridBagConstraints();
		gbc_btnPrint.fill = GridBagConstraints.BOTH;
		gbc_btnPrint.insets = new Insets(0, 0, 0, 5);
		gbc_btnPrint.gridx = 1;
		gbc_btnPrint.gridy = 1;
		add(btnPrint, gbc_btnPrint);
		updateDate();
	}

	private void updateDate() {
		Calendar currentDate = Calendar.getInstance();
		int month = currentDate.get(Calendar.MONTH);
		int year = currentDate.get(Calendar.YEAR);
		monthSelector.setSelectedIndex(month);
		for (int x = 0; x < years.length; x++) {
			if (Integer.parseInt(years[x]) == year)
				yearSelector.setSelectedIndex(x);
		}
	}

	public void setReportPrintService(ReportPrintService reportPrintService) {
		this.reportPrintService = reportPrintService;
	}

	private void printReport() {
		TimeRecordingReportCreator creator = new TimeRecordingReportCreator(employees);
		List<TimeRecordingReportModel> reports = creator.createReports(monthSelector.getSelectedIndex(),
				Integer.parseInt((String)yearSelector.getSelectedItem()));
		for (TimeRecordingReportModel report : reports) {
			Map<String, Object> env = new HashMap<String, Object>();
			env.put("report", report);
			reportPrintService.print(env);
		}
		MessageInf msg = new MessageInf(MessageInf.SGN_SUCCESS, "OK");
		msg.show(this);
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
}

package org.openpos.timerecording;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ListDataEvent;

import org.openpos.CancelException;
import org.openpos.OpenPos;
import org.openpos.OpenPosException;
import org.openpos.print.ReportPrintService;
import org.openpos.reports.closemonth.EmptyListDataListener;
import org.openpos.timerecording.DateSelectorComponent.DateChangedListener;
import org.openpos.timerecording.dao.DaoManager;
import org.openpos.ui.components.TimeKeeper;
import org.openpos.ui.components.TimeSelectorComponent;
import org.openpos.ui.components.TimeSelectorComponent.TimeChangedListener;
import org.openpos.utils.DateTimeUtils;
import org.springframework.util.StringUtils;

import com.openbravo.beans.JPasswordDialog;
import com.openbravo.data.gui.MessageInf;

public class TimeRecordingView extends JPanel implements TimeChangedListener, DateChangedListener {

	private TimeSelectorComponent commingTime;
	private TimeSelectorComponent leavingTime;
	private DateSelectorComponent dateSelectorComponent;
	private JComboBox employeeSelector;
	private TimeSelectorComponent pauseDuration;

	private TimeRecordingModel timeRecordingModel = new TimeRecordingModel();
	private JLabel workingTimeLabel;

	private String[] employeeNames;
	private List<Employee> employees;
	private Employee selectedEmployee;
	private JButton printAndSaveButton;
	private ReportPrintService reportPrintService;

	private DaoManager daoManager;

	public TimeRecordingView() {
		this.daoManager = OpenPos.getApplicationContext().getBean(DaoManager.class);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 200, 277, 0 };
		gridBagLayout.rowHeights = new int[] { 51, 51, 51, 51, 51, 51, 51, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.fill = GridBagConstraints.BOTH;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		add(lblName, gbc_lblName);

		employeeSelector = new JComboBox();
		employeeSelector.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 0;
		add(employeeSelector, gbc_panel_3);

		JLabel lblNewLabel = new JLabel("Datum:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		add(lblNewLabel, gbc_lblNewLabel);

		dateSelectorComponent = new DateSelectorComponent();
		dateSelectorComponent.addDateChangedListener(this);
		GridBagConstraints gbc_dateSelectorComponent = new GridBagConstraints();
		gbc_dateSelectorComponent.fill = GridBagConstraints.BOTH;
		gbc_dateSelectorComponent.insets = new Insets(0, 0, 5, 0);
		gbc_dateSelectorComponent.gridx = 1;
		gbc_dateSelectorComponent.gridy = 1;
		add(dateSelectorComponent, gbc_dateSelectorComponent);

		JLabel lblGekommen = new JLabel("Gekommen:");
		lblGekommen.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblGekommen = new GridBagConstraints();
		gbc_lblGekommen.fill = GridBagConstraints.BOTH;
		gbc_lblGekommen.insets = new Insets(0, 0, 5, 5);
		gbc_lblGekommen.gridx = 0;
		gbc_lblGekommen.gridy = 2;
		add(lblGekommen, gbc_lblGekommen);

		commingTime = new TimeSelectorComponent();
		commingTime.addTimeChangedListener(this);
		GridBagConstraints gbc_timeSelectorComponent = new GridBagConstraints();
		gbc_timeSelectorComponent.fill = GridBagConstraints.BOTH;
		gbc_timeSelectorComponent.insets = new Insets(0, 0, 5, 0);
		gbc_timeSelectorComponent.gridx = 1;
		gbc_timeSelectorComponent.gridy = 2;
		add(commingTime, gbc_timeSelectorComponent);

		JLabel lblGegangen = new JLabel("Gegangen:");
		lblGegangen.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblGegangen = new GridBagConstraints();
		gbc_lblGegangen.fill = GridBagConstraints.BOTH;
		gbc_lblGegangen.insets = new Insets(0, 0, 5, 5);
		gbc_lblGegangen.gridx = 0;
		gbc_lblGegangen.gridy = 3;
		add(lblGegangen, gbc_lblGegangen);

		leavingTime = new TimeSelectorComponent();
		leavingTime.addTimeChangedListener(this);
		GridBagConstraints gbc_timeSelectorComponent_1 = new GridBagConstraints();
		gbc_timeSelectorComponent_1.fill = GridBagConstraints.BOTH;
		gbc_timeSelectorComponent_1.insets = new Insets(0, 0, 5, 0);
		gbc_timeSelectorComponent_1.gridx = 1;
		gbc_timeSelectorComponent_1.gridy = 3;
		add(leavingTime, gbc_timeSelectorComponent_1);

		JLabel lblPausenInMinuten = new JLabel("Pause:");
		lblPausenInMinuten.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblPausenInMinuten = new GridBagConstraints();
		gbc_lblPausenInMinuten.fill = GridBagConstraints.BOTH;
		gbc_lblPausenInMinuten.insets = new Insets(0, 0, 5, 5);
		gbc_lblPausenInMinuten.gridx = 0;
		gbc_lblPausenInMinuten.gridy = 4;
		add(lblPausenInMinuten, gbc_lblPausenInMinuten);

		pauseDuration = new TimeSelectorComponent();
		pauseDuration.addTimeChangedListener(this);
		GridBagConstraints gbc_pauseDuration = new GridBagConstraints();
		gbc_pauseDuration.fill = GridBagConstraints.BOTH;
		gbc_pauseDuration.insets = new Insets(0, 0, 5, 0);
		gbc_pauseDuration.gridx = 1;
		gbc_pauseDuration.gridy = 4;
		add(pauseDuration, gbc_pauseDuration);

		JLabel lblArbeitszeitInStunden = new JLabel("Arbeitszeit in Stunden:");
		lblArbeitszeitInStunden.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblArbeitszeitInStunden = new GridBagConstraints();
		gbc_lblArbeitszeitInStunden.fill = GridBagConstraints.BOTH;
		gbc_lblArbeitszeitInStunden.insets = new Insets(0, 0, 5, 5);
		gbc_lblArbeitszeitInStunden.gridx = 0;
		gbc_lblArbeitszeitInStunden.gridy = 5;
		add(lblArbeitszeitInStunden, gbc_lblArbeitszeitInStunden);

		workingTimeLabel = new JLabel("5:30");
		workingTimeLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		workingTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_workingTimeLabel = new GridBagConstraints();
		gbc_workingTimeLabel.insets = new Insets(0, 0, 5, 0);
		gbc_workingTimeLabel.fill = GridBagConstraints.BOTH;
		gbc_workingTimeLabel.gridx = 1;
		gbc_workingTimeLabel.gridy = 5;
		add(workingTimeLabel, gbc_workingTimeLabel);

		printAndSaveButton = new JButton("Drucken und Speichern");
		printAndSaveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					printAndSave();
				}
				catch (CancelException cancelException) {
				}
				catch (OpenPosException ope) {
					MessageInf msg = new MessageInf(MessageInf.CLS_GENERIC, ope.getMessage());
					msg.show(TimeRecordingView.this);
				}
			}
		});
		printAndSaveButton.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_printAndSaveButton = new GridBagConstraints();
		gbc_printAndSaveButton.fill = GridBagConstraints.VERTICAL;
		gbc_printAndSaveButton.gridx = 1;
		gbc_printAndSaveButton.gridy = 6;
		add(printAndSaveButton, gbc_printAndSaveButton);

		initFields();
	}

	public TimeRecordingModel getTimeRecordingModel() {
		return timeRecordingModel;
	}

	public void setTimeRecordingModel(TimeRecordingModel timeRecordingModel) {
		this.timeRecordingModel = timeRecordingModel;
		initFields();
	}

	public void resetFields() {
		timeRecordingModel = new TimeRecordingModel();
		initFields();
		if (employees != null && !employees.isEmpty()) {
			employeeSelector.setSelectedIndex(0);
			setSelectedEmployee(employees.get(0));
		}
	}

	private void initFields() {
		int minutes = getCurrentMinutes();
		if (timeRecordingModel.getCommingTime() == 0)
			timeRecordingModel.setCommingTime(minutes);

		if (timeRecordingModel.getLeavingTime() == 0)
			timeRecordingModel.setLeavingTime(minutes);

		commingTime.setTime(timeRecordingModel.getCommingTime());
		leavingTime.setTime(timeRecordingModel.getLeavingTime());
		pauseDuration.setTime(timeRecordingModel.getPauseDuration());

		dateSelectorComponent.setCurrentDate(timeRecordingModel.getDate());
		updateWorkingTime();
	}

	private int getCurrentMinutes() {
		Calendar now = Calendar.getInstance();
		int minutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);
		return minutes;
	}

	@Override
	public void onTimeChanged(TimeKeeper timeSelectorModel, TimeSelectorComponent source) {
		if (source.equals(commingTime)) {
			timeRecordingModel.setCommingTime(commingTime.getTime());
		}
		else if (source.equals(leavingTime)) {
			timeRecordingModel.setLeavingTime(leavingTime.getTime());
		}
		else if (source.equals(pauseDuration)) {
			timeRecordingModel.setPauseDuration(pauseDuration.getTime());
		}
		updateWorkingTime();
	}

	@Override
	public void onDateChanged(Calendar calendar, DateSelectorComponent source) {
		timeRecordingModel.setDate(calendar);
	}

	private void updateWorkingTime() {
		int workingTime = DateTimeUtils.calcWorkingTime(timeRecordingModel.getCommingTime(),
				timeRecordingModel.getLeavingTime(), timeRecordingModel.getPauseDuration());
		timeRecordingModel.setWorkingTime(workingTime);

		if (workingTime >= 0)
			workingTimeLabel.setText(DateTimeUtils.formatTime(workingTime));
		else
			workingTimeLabel.setText("");
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
		employeeNames = new String[employees.size()];
		for (int x = 0; x < employeeNames.length; x++) {
			employeeNames[x] = employees.get(x).getName();
		}

		ComboBoxModel comboBoxModel = new DefaultComboBoxModel(employeeNames);
		comboBoxModel.addListDataListener(new EmptyListDataListener() {

			@Override
			public void contentsChanged(ListDataEvent e) {
				setSelectedEmployee(TimeRecordingView.this.employees.get(employeeSelector.getSelectedIndex()));
			}
		});
		employeeSelector.setModel(comboBoxModel);
		setSelectedEmployee(employees.get(0));
	}

	public void setSelectedEmployee(Employee selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
		timeRecordingModel.setEmployeeName(selectedEmployee.getName());
	}

	public void setReportPrintService(ReportPrintService reportPrintService) {
		this.reportPrintService = reportPrintService;
	}

	private void saveTimeRecording() {
		daoManager.insertTimeRecording(timeRecordingModel);
	}

	private void updateTimeRecording() {
		daoManager.updateTimeRecording(timeRecordingModel);
	}

	private boolean previousEntryExists() {
		Date date = timeRecordingModel.getDate().getTime();
		return !daoManager.selectTimeRecordingModel(timeRecordingModel.getEmployeeName(), date, date).isEmpty();
	}

	private void printAndSave() {
		if (timeRecordingModel.isValid()) {

			String pin = JPasswordDialog.showEditPassword(this, "PIN", "Bitte Geburtsjahr zur Bestätigung eingeben",
					null, true);

			if (!StringUtils.hasText(pin))
				throw new CancelException();

			if (pin.equals(selectedEmployee.getPin())) {
				updateEarnings();
				if (previousEntryExists()) {
					int result = JOptionPane.showConfirmDialog(this,
							"Es existieren bereits Zeiterfassungsdaten für diesen Tag.\n"
									+ "Sollen die vorherigen Daten überschrieben werden?", "Zeiterfassung",
							JOptionPane.OK_CANCEL_OPTION);

					if (result == JOptionPane.OK_OPTION) {
						updateTimeRecording();
					}
					else
						throw new CancelException();
				}
				else {
					saveTimeRecording();
				}
				printReport();
			}
			else {
				throw new OpenPosException("Die Pin ist nicht korrekt (" + pin + ").");
			}
		}
		else {
			throw new OpenPosException("Daten unvollständig");
		}
	}

	private void printReport() {
		Map<String, Object> env = new HashMap<String, Object>();
		env.put("trm", new TimeRecordingModelFormatter().formatModel(timeRecordingModel));
		reportPrintService.print(env);
	}

	private void updateEarnings() {
		double earnings = (double)timeRecordingModel.getWorkingTime() * ((double)selectedEmployee.getWageRate() / 60.0);

		timeRecordingModel.setEarningsInCents((int)Math.round(earnings * 100));
	}
}

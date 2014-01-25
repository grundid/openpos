package org.openpos.reports.cash;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.openpos.reports.CashFromToModel;
import org.openpos.timerecording.DateSelectorComponent;
import org.openpos.timerecording.DateSelectorComponent.DateChangedListener;
import org.openpos.ui.components.TimeKeeper;
import org.openpos.ui.components.TimeSelectorComponent;
import org.openpos.ui.components.TimeSelectorComponent.TimeChangedListener;

public class CashFromToView extends JPanel implements TimeChangedListener, DateChangedListener {

	private TimeSelectorComponent fromTime;
	private TimeSelectorComponent toTime;
	private DateSelectorComponent dateSelectorComponent;
	private CashFromToModel cashFromToModel = new CashFromToModel();

	public CashFromToView() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 200, 277, 0 };
		gridBagLayout.rowHeights = new int[] { 51, 51, 51, 51, 51, 51, 51, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
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
		JLabel lblGekommen = new JLabel("Von:");
		lblGekommen.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblGekommen = new GridBagConstraints();
		gbc_lblGekommen.fill = GridBagConstraints.BOTH;
		gbc_lblGekommen.insets = new Insets(0, 0, 5, 5);
		gbc_lblGekommen.gridx = 0;
		gbc_lblGekommen.gridy = 2;
		add(lblGekommen, gbc_lblGekommen);
		fromTime = new TimeSelectorComponent();
		fromTime.addTimeChangedListener(this);
		GridBagConstraints gbc_timeSelectorComponent = new GridBagConstraints();
		gbc_timeSelectorComponent.fill = GridBagConstraints.BOTH;
		gbc_timeSelectorComponent.insets = new Insets(0, 0, 5, 0);
		gbc_timeSelectorComponent.gridx = 1;
		gbc_timeSelectorComponent.gridy = 2;
		add(fromTime, gbc_timeSelectorComponent);
		JLabel lblGegangen = new JLabel("Bis:");
		lblGegangen.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblGegangen = new GridBagConstraints();
		gbc_lblGegangen.fill = GridBagConstraints.BOTH;
		gbc_lblGegangen.insets = new Insets(0, 0, 5, 5);
		gbc_lblGegangen.gridx = 0;
		gbc_lblGegangen.gridy = 3;
		add(lblGegangen, gbc_lblGegangen);
		toTime = new TimeSelectorComponent();
		toTime.addTimeChangedListener(this);
		GridBagConstraints gbc_timeSelectorComponent_1 = new GridBagConstraints();
		gbc_timeSelectorComponent_1.fill = GridBagConstraints.BOTH;
		gbc_timeSelectorComponent_1.insets = new Insets(0, 0, 5, 0);
		gbc_timeSelectorComponent_1.gridx = 1;
		gbc_timeSelectorComponent_1.gridy = 3;
		add(toTime, gbc_timeSelectorComponent_1);
		initFields();
	}

	public void resetFields() {
		cashFromToModel = new CashFromToModel();
		initFields();
	}

	private void initFields() {
		//		int minutes = getCurrentMinutes();
		if (cashFromToModel.getFromTime() == 0)
			cashFromToModel.setFromTime(8 * 60);
		if (cashFromToModel.getToTime() == 0)
			cashFromToModel.setToTime(14 * 60);
		fromTime.setTime(cashFromToModel.getFromTime());
		toTime.setTime(cashFromToModel.getToTime());
		dateSelectorComponent.setCurrentDate(cashFromToModel.getDate());
	}

	public CashFromToModel getCashFromToModel() {
		return cashFromToModel;
	}

	private int getCurrentMinutes() {
		Calendar now = Calendar.getInstance();
		int minutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);
		return minutes;
	}

	@Override
	public void onTimeChanged(TimeKeeper timeSelectorModel, TimeSelectorComponent source) {
		if (source.equals(fromTime)) {
			cashFromToModel.setFromTime(fromTime.getTime());
		}
		else if (source.equals(toTime)) {
			cashFromToModel.setToTime(toTime.getTime());
		}
	}

	@Override
	public void onDateChanged(Calendar calendar, DateSelectorComponent source) {
		cashFromToModel.setDate(calendar);
	}

	public void setFromToTime(int newFromTime, int newToTime) {
		cashFromToModel.setFromTime(newFromTime);
		cashFromToModel.setToTime(newToTime);
		fromTime.setTime(cashFromToModel.getFromTime());
		toTime.setTime(cashFromToModel.getToTime());
	}
}

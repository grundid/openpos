package org.openpos.timerecording;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.openpos.reports.closemonth.EmptyListDataListener;
import org.openpos.utils.DateTimeUtils;

public class DateSelectorComponent extends JPanel {

	private Calendar currentDate = Calendar.getInstance();

	private ListDataListener comboBoxChangedListener = new EmptyListDataListener() {

		@Override
		public void contentsChanged(ListDataEvent e) {
			dateChanged();
		}
	};

	private Collection<DateChangedListener> listenerList = new ArrayList<DateChangedListener>();

	public static interface DateChangedListener {

		void onDateChanged(Calendar calendar, DateSelectorComponent source);
	}

	private JComboBox daySelector;
	private JComboBox monthSelector;
	private JComboBox yearSelector;
	private String[] years = DateTimeUtils.createYearsArray(2011);

	public DateSelectorComponent() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 75, 150, 75, 0 };
		gridBagLayout.rowHeights = new int[] { 50, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		daySelector = new JComboBox(DateTimeUtils.createDayArray());
		daySelector.getModel().addListDataListener(comboBoxChangedListener);

		daySelector.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_daySelector = new GridBagConstraints();
		gbc_daySelector.fill = GridBagConstraints.BOTH;
		gbc_daySelector.insets = new Insets(0, 0, 0, 5);
		gbc_daySelector.gridx = 0;
		gbc_daySelector.gridy = 0;
		add(daySelector, gbc_daySelector);

		monthSelector = new JComboBox(DateTimeUtils.createMonthArray());
		monthSelector.getModel().addListDataListener(comboBoxChangedListener);
		monthSelector.setMaximumRowCount(12);
		monthSelector.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_monthSelector = new GridBagConstraints();
		gbc_monthSelector.fill = GridBagConstraints.BOTH;
		gbc_monthSelector.insets = new Insets(0, 0, 0, 5);
		gbc_monthSelector.gridx = 1;
		gbc_monthSelector.gridy = 0;
		add(monthSelector, gbc_monthSelector);

		yearSelector = new JComboBox(years);
		yearSelector.getModel().addListDataListener(comboBoxChangedListener);
		yearSelector.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_yearSelector = new GridBagConstraints();
		gbc_yearSelector.fill = GridBagConstraints.BOTH;
		gbc_yearSelector.gridx = 2;
		gbc_yearSelector.gridy = 0;
		add(yearSelector, gbc_yearSelector);

		updateDate();
	}

	private void dateChanged() {
		int day = daySelector.getSelectedIndex() + 1;
		int month = monthSelector.getSelectedIndex();
		int year = Integer.parseInt((String)yearSelector.getSelectedItem());
		currentDate.set(year, month, day);
		for (DateChangedListener listener : listenerList)
			listener.onDateChanged(currentDate, this);
	}

	private void updateDate() {
		int day = currentDate.get(Calendar.DAY_OF_MONTH);
		int month = currentDate.get(Calendar.MONTH);
		int year = currentDate.get(Calendar.YEAR);
		daySelector.setSelectedIndex(day - 1);
		monthSelector.setSelectedIndex(month);
		for (int x = 0; x < years.length; x++) {
			if (Integer.parseInt(years[x]) == year)
				yearSelector.setSelectedIndex(x);
		}
	}

	public Calendar getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Calendar currentDate) {
		this.currentDate = currentDate;
		updateDate();
	}

	public void addDateChangedListener(DateChangedListener dateChangedListener) {
		listenerList.add(dateChangedListener);
	}
}

package org.openpos.ui.components;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.openpos.utils.DateTimeUtils;

public class TimeSelectorComponent extends JPanel implements ActionListener {

	private JTextField timeField;
	private Font font = new Font("Tahoma", Font.BOLD, 18);
	private TimeKeeper timeKeeper = new TimeKeeper();
	private Collection<TimeChangedListener> listenerList = new ArrayList<TimeChangedListener>();

	public static interface TimeChangedListener {

		void onTimeChanged(TimeKeeper timeSelectorModel, TimeSelectorComponent source);
	}

	public TimeSelectorComponent() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 55, 55, 90, 55, 50, 0 };
		gridBagLayout.rowHeights = new int[] { 50, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JButton minusHour = new JButton("-");
		minusHour.setActionCommand(TimeSelectorCommands.DEC_HOUR.name());
		minusHour.addActionListener(this);
		minusHour.setFont(font);
		GridBagConstraints gbc_minusHour = new GridBagConstraints();
		gbc_minusHour.fill = GridBagConstraints.BOTH;
		gbc_minusHour.insets = new Insets(0, 0, 0, 5);
		gbc_minusHour.gridx = 0;
		gbc_minusHour.gridy = 0;
		add(minusHour, gbc_minusHour);

		JButton plusHour = new JButton("+");
		plusHour.setActionCommand(TimeSelectorCommands.INC_HOUR.name());
		plusHour.addActionListener(this);
		plusHour.setFont(font);
		GridBagConstraints gbc_plusHour = new GridBagConstraints();
		gbc_plusHour.fill = GridBagConstraints.BOTH;
		gbc_plusHour.insets = new Insets(0, 0, 0, 5);
		gbc_plusHour.gridx = 1;
		gbc_plusHour.gridy = 0;
		add(plusHour, gbc_plusHour);

		timeField = new JTextField();
		timeField.setHorizontalAlignment(SwingConstants.CENTER);
		timeField.setFont(font);
		GridBagConstraints gbc_timeField = new GridBagConstraints();
		gbc_timeField.fill = GridBagConstraints.VERTICAL;
		gbc_timeField.anchor = GridBagConstraints.WEST;
		gbc_timeField.insets = new Insets(0, 0, 0, 5);
		gbc_timeField.gridx = 2;
		gbc_timeField.gridy = 0;
		add(timeField, gbc_timeField);
		timeField.setColumns(5);

		JButton plusMinute = new JButton("+");
		plusMinute.setActionCommand(TimeSelectorCommands.INC_MINUTE.name());
		plusMinute.addActionListener(this);
		plusMinute.setFont(font);
		GridBagConstraints gbc_plusMinute = new GridBagConstraints();
		gbc_plusMinute.fill = GridBagConstraints.BOTH;
		gbc_plusMinute.gridx = 4;
		gbc_plusMinute.gridy = 0;
		add(plusMinute, gbc_plusMinute);

		JButton minusMinute = new JButton("-");
		minusMinute.setActionCommand(TimeSelectorCommands.DEC_MINUTE.name());
		minusMinute.addActionListener(this);
		minusMinute.setFont(font);
		GridBagConstraints gbc_minusMinute = new GridBagConstraints();
		gbc_minusMinute.fill = GridBagConstraints.BOTH;
		gbc_minusMinute.insets = new Insets(0, 0, 0, 5);
		gbc_minusMinute.gridx = 3;
		gbc_minusMinute.gridy = 0;
		add(minusMinute, gbc_minusMinute);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		TimeSelectorCommands command = TimeSelectorCommands.valueOf(e.getActionCommand());

		switch (command) {
			case INC_HOUR:
				timeKeeper.incHour();
				break;
			case INC_MINUTE:
				timeKeeper.incMinute();
				break;
			case DEC_HOUR:
				timeKeeper.decHour();
				break;
			case DEC_MINUTE:
				timeKeeper.decMinute();
				break;
		}
		timeChanged();
	}

	private void timeChanged() {
		timeField.setText(DateTimeUtils.formatTime(timeKeeper.getHour(), timeKeeper.getMinute()));

		for (TimeChangedListener listener : listenerList)
			listener.onTimeChanged(timeKeeper, this);
	}

	public void setTime(int time) {
		timeKeeper.setTime(time);
		timeChanged();
	}

	public int getTime() {
		return timeKeeper.getTime();
	}

	public void addTimeChangedListener(TimeChangedListener timeChangedListener) {
		listenerList.add(timeChangedListener);
	}

}

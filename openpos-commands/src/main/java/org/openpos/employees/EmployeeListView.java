package org.openpos.employees;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListDataEvent;

import org.openpos.reports.closemonth.EmptyListDataListener;
import org.openpos.timerecording.Employee;
import org.openpos.timerecording.EmployeeDataBase;
import org.openpos.timerecording.EmployeeListResources;
import org.springframework.util.StringUtils;

import com.openbravo.data.gui.MessageInf;

public class EmployeeListView extends JPanel {

	private JComboBox employeeSelector;
	private JTextField nameField;
	private JTextField wageField;
	private JTextField pinField;

	private String[] employeeNames;
	private List<Employee> employees;
	private Employee selectedEmployee;
	private JButton saveButton;
	private JButton deleteButton;
	private JButton newEmployeeButton;

	private EmployeeListResources employeeListResources;
	private EmployeeDataBase employeeDataBase;

	private DecimalFormat df = new DecimalFormat("#,###.0");

	public EmployeeListView(EmployeeListResources employeeListResources) {
		this.employeeListResources = employeeListResources;
		employeeDataBase = employeeListResources.getEmployeeDataBase();
		initViews();
	}

	private void initViews() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 200, 200, 200 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0 };

		gridBagLayout.rowHeights = new int[] { 51, 51, 51, 51, 51, 51, 51, 0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblEmployee = new JLabel("Mitarbeiter:");
		lblEmployee.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.fill = GridBagConstraints.BOTH;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		add(lblEmployee, gbc_lblName);

		employeeSelector = new JComboBox();
		employeeSelector.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridwidth = GridBagConstraints.REMAINDER;
		gbc_panel_3.gridy = 0;
		add(employeeSelector, gbc_panel_3);

		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		add(lblName, gbc_lblNewLabel);

		nameField = new JTextField();
		nameField.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbcNameField = new GridBagConstraints();
		gbcNameField.fill = GridBagConstraints.BOTH;
		gbcNameField.insets = new Insets(0, 0, 5, 0);
		gbcNameField.gridwidth = GridBagConstraints.REMAINDER;
		gbcNameField.gridx = 1;
		gbcNameField.gridy = 1;
		add(nameField, gbcNameField);

		wageField = createInputLabelField("Stundenlohn:", 2);
		pinField = createInputLabelField("Geburtsjahr:", 3);

		saveButton = createButton("Speichern", 0, 4);
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveEmployee();
			}
		});

		deleteButton = createButton("Löschen", 1, 4);
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteEmployee();
			}

		});

		newEmployeeButton = createButton("Neuer Mitarbeiter", 2, 4);
		newEmployeeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				newEmployee();

			}
		});
	}

	private JTextField createInputLabelField(String labelText, int row) {
		JLabel label = new JLabel(labelText);
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbcLabel = new GridBagConstraints();
		gbcLabel.fill = GridBagConstraints.BOTH;
		gbcLabel.insets = new Insets(0, 0, 5, 5);
		gbcLabel.gridx = 0;
		gbcLabel.gridy = row;
		add(label, gbcLabel);

		JTextField textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbcTextField = new GridBagConstraints();
		gbcTextField.fill = GridBagConstraints.BOTH;
		gbcTextField.insets = new Insets(0, 0, 5, 0);
		gbcTextField.gridwidth = GridBagConstraints.REMAINDER;
		gbcTextField.gridx = 1;
		gbcTextField.gridy = row;
		add(textField, gbcTextField);
		return textField;
	}

	protected void newEmployee() {
		resetModel();
	}

	private JButton createButton(String label, int gridX, int gridY) {
		JButton button = new JButton(label);
		button.setFont(new Font("Tahoma", Font.BOLD, 18));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.gridx = gridX;
		gbc.gridy = gridY;
		add(button, gbc);

		return button;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
		updateEmployeeList();
		setSelectedEmployee(employees.get(0));
	}

	private void updateEmployeeList() {
		employeeNames = new String[employees.size()];
		for (int x = 0; x < employeeNames.length; x++) {
			Employee employee = employees.get(x);
			employeeNames[x] = employee.getName() + " (" + df.format(employee.getWageRate()) + ")";
		}

		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(employeeNames);
		comboBoxModel.addListDataListener(new EmptyListDataListener() {

			@Override
			public void contentsChanged(ListDataEvent e) {
				setSelectedEmployee(EmployeeListView.this.employees.get(employeeSelector.getSelectedIndex()));
			}
		});

		employeeSelector.setModel(comboBoxModel);
	}

	public void setSelectedEmployee(Employee selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
		nameField.setText(selectedEmployee.getName());
		wageField.setText(df.format(selectedEmployee.getWageRate()));
		pinField.setText(selectedEmployee.getPin());
	}

	public void setModelData() throws ParseException {
		if (StringUtils.hasText(nameField.getText())) {
			selectedEmployee.setName(nameField.getText());
		}
		else {
			throw new ParseException("Mitarbeitername darf nicht leer sein.", 0);
		}
		selectedEmployee.setWageRate(df.parse(wageField.getText()).doubleValue());
		selectedEmployee.setPin(pinField.getText());
	}

	private void saveEmployee() {
		try {
			setModelData();
			employeeDataBase.addOrUpdateEmployee(selectedEmployee);
			employeeListResources.persistEmployeeDataBase();
			updateEmployeeList();
			resetModel();
			MessageInf msg = new MessageInf(MessageInf.SGN_SUCCESS, "OK");
			msg.show(this);

		}
		catch (ParseException e) {
			MessageInf msg = new MessageInf(MessageInf.CLS_GENERIC, "Daten unvollständig: " + e.getMessage());
			msg.show(this);
		}
	}

	private void resetModel() {
		selectedEmployee = new Employee("", 0);
		nameField.setText("");
		wageField.setText("");
		pinField.setText("");
	}

	private void deleteEmployee() {
		int result = JOptionPane.showConfirmDialog(this, "Sicher löschen?", "Mitarbeiter löschen",
				JOptionPane.OK_CANCEL_OPTION);

		if (result == JOptionPane.OK_OPTION) {
			employeeDataBase.removeEmployee(selectedEmployee);
			employeeListResources.persistEmployeeDataBase();
			updateEmployeeList();
			resetModel();
		}
	}

}

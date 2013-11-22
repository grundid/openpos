package org.openpos.employees;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.openpos.timerecording.EmployeeListResources;

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.JPanelView;

public class EmployeeListPanel implements JPanelView, BeanFactoryApp {

	private EmployeeListView view;

	@Override
	public Object getBean() {
		return this;
	}

	@Override
	public void init(AppView app) throws BeanFactoryException {
		EmployeeListResources employeeListResources = new EmployeeListResources(app);
		view = new EmployeeListView(employeeListResources);
		view.setEmployees(employeeListResources.getEmployeeDataBase().getEmployees());
	}

	@Override
	public String getTitle() {
		return "Mitarbeiterliste";
	}

	@Override
	public void activate() throws BasicException {
	}

	@Override
	public boolean deactivate() {
		return true;
	}

	@Override
	public JComponent getComponent() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(view);
		return panel;
	}
}

package com.openbravo.pos.legacy.forms;

import javax.swing.JPanel;

import org.openpos.AppContext;
import org.openpos.events.OnStatusChangedListener;

import com.openbravo.pos.forms.JPrincipalApp;


public class StandardOnStatusChangedListener implements OnStatusChangedListener {

	private JPanel panel;
	
	public StandardOnStatusChangedListener(JPanel panel) {
		this.panel = panel;
	}

	@Override
	public void onAppClose(AppContext appContext) {
		JPrincipalApp principalApp = appContext.getBean(JPrincipalApp.class);
		panel.remove(principalApp.getNotificator());
		repaintPanel();
	}

	private void repaintPanel() {
		panel.revalidate();
		panel.repaint();
	}

	@Override
	public void onAppOpen(AppContext appContext) {
		JPrincipalApp principalApp = appContext.getBean(JPrincipalApp.class);
		panel.add(principalApp.getNotificator());
		repaintPanel();
	}
}

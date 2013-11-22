package com.openbravo.pos.legacy.forms;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.openpos.AppContext;
import org.openpos.notify.OnInitializedNotifier;
import org.openpos.notify.OnStatusChangedNotifier;
import org.openpos.ui.BottomPanelCreator;

public class StandardBottomPanelCreator implements BottomPanelCreator {

	@Override
	public JPanel createBottomPanel(AppContext appContext) {
		JPanel statusPanel = new JPanel();
		JPanel panelTask = new JPanel();

		JPanel panel = new javax.swing.JPanel();
		panel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, javax.swing.UIManager
				.getDefaults().getColor("Button.darkShadow")));
		panel.setLayout(new java.awt.BorderLayout());

		JLabel m_jHost = new JLabel();
		m_jHost.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/display.png"))); // NOI18N
		m_jHost.setText("*Hostname");
		panelTask.add(m_jHost);

		panel.add(panelTask, java.awt.BorderLayout.LINE_START);
		panel.add(statusPanel, java.awt.BorderLayout.LINE_END);

		appContext.getBean(OnStatusChangedNotifier.class).setOnStatusChangedListener(
				new StandardOnStatusChangedListener(statusPanel));
		appContext.getBean(OnInitializedNotifier.class).setOnInitializedListener(
				new StandardOnInitializedListener(m_jHost));
				
		return panel;
	}
}

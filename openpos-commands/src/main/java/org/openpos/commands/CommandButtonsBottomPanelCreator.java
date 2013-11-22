package org.openpos.commands;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import org.openpos.AppContext;
import org.openpos.notify.OnStatusChangedNotifier;
import org.openpos.ui.BottomPanelCreator;

import com.openbravo.pos.legacy.forms.StandardOnStatusChangedListener;

public class CommandButtonsBottomPanelCreator implements BottomPanelCreator {

	@Override
	public JPanel createBottomPanel(AppContext appContext) {
		JPanel panelTask = new JPanel();
		JPanel statusPanel = new JPanel();
		JPanel panel = new javax.swing.JPanel();

		panel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, javax.swing.UIManager.getDefaults()
				.getColor("Button.darkShadow")));
		panel.setLayout(new FlowLayout());

		panel.add(panelTask, java.awt.BorderLayout.LINE_START);
		CommandPosUtils.addBottomButtons(panel);
		panel.add(statusPanel);

		appContext.getBean(OnStatusChangedNotifier.class).setOnStatusChangedListener(
				new StandardOnStatusChangedListener(statusPanel));

		return panel;
	}
}

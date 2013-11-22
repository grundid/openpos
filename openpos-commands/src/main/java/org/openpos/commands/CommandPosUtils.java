package org.openpos.commands;

import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.openpos.commands.ExecCommands.Command;
import org.openpos.wifi.WifiReceiptCommand;

/**
 * @author adrian
 * @since 23.04.2009
 */
public class CommandPosUtils {

	private static ResourceBundle rb = ResourceBundle.getBundle("openpos-commands");

	public static String getResource(String key) {
		try {
			return rb.getString(key);
		}
		catch (Exception e) {
			return key;
		}
	}

	public static JButton createButton(String titleKey, ActionListener actionListener) {
		JButton button = new JButton();
		button.setText(getResource(titleKey));
		button.setFocusPainted(false);
		button.setFocusable(false);
		button.setMargin(new java.awt.Insets(8, 14, 8, 14));
		button.setRequestFocusEnabled(false);
		button.addActionListener(actionListener);
		return button;
	}

	public static void addBottomButtons(JPanel panel) {
		panel.add(CommandPosUtils.createButton("sound.volume", ExecCommands.createActionListener(Command.SOUND_VOLUME)));
		panel.add(CommandPosUtils.createButton("sound.standard",
				ExecCommands.createActionListener(Command.MUSIC_STANDARD)));
		panel.add(CommandPosUtils.createButton("sound.special1",
				ExecCommands.createActionListener(Command.MUSIC_SPECIAL1)));
		panel.add(CommandPosUtils.createButton("sound.special2",
				ExecCommands.createActionListener(Command.MUSIC_SPECIAL2)));
		panel.add(CommandPosUtils.createButton("sound.stop", ExecCommands.createActionListener(Command.MUSIC_STOP)));
		panel.add(CommandPosUtils.createButton("wifi.receipt", new WifiReceiptCommand()));
	}
}

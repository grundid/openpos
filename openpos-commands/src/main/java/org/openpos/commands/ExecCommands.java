package org.openpos.commands;

import java.awt.event.ActionListener;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ExecCommands {

	public static enum Command {
		MUSIC_STANDARD, MUSIC_SPECIAL1, MUSIC_SPECIAL2, MUSIC_STOP, SOUND_VOLUME;
	}

	private static ResourceBundle rb = ResourceBundle.getBundle("external-apps");

	public static ActionListener createActionListener(Command command) {
		return new ExecActionListener(createCommand(command));
	}

	private static String createCommand(Command command) {
		try {
			switch (command) {
				case MUSIC_STANDARD:
					return rb.getString("app.music.player") + " " + rb.getString("data.music.standard");
				case MUSIC_SPECIAL1:
					return rb.getString("app.music.player") + " " + rb.getString("data.music.special1");
				case MUSIC_SPECIAL2:
					return rb.getString("app.music.player") + " " + rb.getString("data.music.special2");
				case MUSIC_STOP:
					return rb.getString("app.stop.music.player");
				case SOUND_VOLUME:
					return rb.getString("app.sound.volume");
			}
		}
		catch (MissingResourceException e) {
			return "";
		}
		throw new RuntimeException("unknown command: " + command);
	}
}

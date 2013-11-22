package org.openpos.commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Logger;

public class ExecActionListener implements ActionListener {

	private String command;
	private Logger log = Logger.getLogger(ExecActionListener.class.getName());

	public ExecActionListener(String command) {
		this.command = command;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			Runtime.getRuntime().exec(command);
		}
		catch (IOException e) {
			log.severe(e.getMessage());
		}
	}
}

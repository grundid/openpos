package org.openpos;

import java.awt.EventQueue;

public class OpenPosLauncher {

	public static void main(String[] args) {
		OpenPos.initApplicationContext("org.openpos", "com.openbravo");
		StartPOS startPOS = OpenPos.getApplicationContext().getBean(StartPOS.class);
		EventQueue.invokeLater(startPOS);
	}
}

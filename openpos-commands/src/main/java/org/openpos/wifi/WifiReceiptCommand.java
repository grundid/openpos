package org.openpos.wifi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.openpos.OpenPos;

public class WifiReceiptCommand implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		OpenPos.getApplicationContext().getBean(WifiService.class).printCurrentWifiPassword();
	}
}

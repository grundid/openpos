package org.openpos.wifi;

import org.openpos.utils.ResourcesHandler;

import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptFactory;

public class WifiResources extends ResourcesHandler {

	private static final String WIFI_KEY = "Wifi.Key";
	private static final String WIFI_SSID = "Wifi.Ssid";
	private static final String WIFI_LAST_MODIFIED = "Wifi.LastModified";
	private static final String WIFI_PROMOTION = "Wifi.Promotion";
	private static final String WIFI_RECEIPT = "Wifi.Receipt";
	private static final String FRITZ_PASSWORT = "Fritz.Passwort";
	private TicketParser ticketParser;

	public WifiResources(AppView appView) {
		super(appView);
		ticketParser = new TicketParser(appView.getDeviceTicket(), dataLogicSystem);
	}

	public void printWifiReceipt() {
		try {
			ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
			script.put("wifi", new WifiConfig(getSsid(), getWifiKey(), getPromotion()));
			String evaluatedReceipt = script.eval(getResourceAsString(WIFI_RECEIPT)).toString();
			ticketParser.printTicket(evaluatedReceipt);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean hasWifiKey() {
		return !"".equals(getWifiKey());
	}

	public String getWifiKey() {
		return getResourceAsString(WIFI_KEY);
	}

	public String getPromotion() {
		return getResourceAsString(WIFI_PROMOTION);
	}

	public String getSsid() {
		return getResourceAsString(WIFI_SSID);
	}

	public void setWifiKey(String key) {
		setResourceAsString(WIFI_KEY, key);
	}

	public long getLastModified() {
		String lm = getResourceAsString(WIFI_LAST_MODIFIED);
		return Long.parseLong(lm);
	}

	public void setLastModified(long time) {
		setResourceAsString(WIFI_LAST_MODIFIED, String.valueOf(time));
	}

	public String getFritzPasswort() {
		return getResourceAsString(FRITZ_PASSWORT);
	}
}

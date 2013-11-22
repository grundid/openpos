package org.openpos.wifi;

import java.util.Calendar;
import java.util.logging.Logger;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.openbravo.pos.forms.AppView;

import de.grundid.fritz.FritzTemplate;

@Service
public class WifiService {

	private Logger log = Logger.getLogger(WifiService.class.getName());
	private WifiResources wifiResources;

	public void initAppView(AppView appView) {
		wifiResources = new WifiResources(appView);
		afterPropertiesSet();
	}

	public void afterPropertiesSet() {
		try {
			if (wifiResources.hasWifiKey()) {
				Calendar c = Calendar.getInstance();
				int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
				long now = c.getTimeInMillis();
				long lastModified = wifiResources.getLastModified();
				double lastModifiedHours = ((double)(now - lastModified)) / (1000 * 60 * 60);
				if (lastModifiedHours > 24 && dayOfWeek == Calendar.WEDNESDAY) {
					updateWifi();
				}
				else
					activateWifi(wifiResources.getWifiKey());
			}
			else
				updateWifi();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateWifi() {
		log.info("Updating Wifi...");
		String newWifiKey = generateNewWifiKey();
		activateWifi(newWifiKey);
		wifiResources.setWifiKey(newWifiKey);
		wifiResources.setLastModified(System.currentTimeMillis());
	}

	private void activateWifi(String wifiKey) {
		log.info("Activating Wifi...");
		FritzTemplate fritzTemplate = new FritzTemplate(new RestTemplate(), wifiResources.getFritzPasswort());
		fritzTemplate.activateGuestAccess(wifiResources.getSsid(), wifiKey);
	}

	public String generateNewWifiKey() {
		return RandomStringUtils.randomAlphanumeric(8);
	}

	public void printCurrentWifiPassword() {
		wifiResources.printWifiReceipt();
	}
}

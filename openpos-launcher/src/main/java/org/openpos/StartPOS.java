package org.openpos;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.api.SubstanceSkin;
import org.openpos.reports.email.MonthyReports;
import org.openpos.utils.LegacyFactory;
import org.openpos.wifi.WifiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.JRootFrame;
import com.openbravo.pos.forms.JRootKiosk;
import com.openbravo.pos.instance.InstanceQuery;

@Component
public class StartPOS implements Runnable {

	private static Logger logger = Logger.getLogger("com.openbravo.pos.forms.StartPOS");
	@Autowired
	private AppConfig config;
	@Autowired
	private LegacyFactory legacyFactory;
	@Autowired
	private MonthyReports monthyReports;
	@Autowired
	private WifiService wifiService;

	public StartPOS() {
	}

	public static boolean registerApp() {
		InstanceQuery i = null;
		try {
			i = new InstanceQuery();
			i.getAppMessage().restoreWindow();
			return false;
		}
		catch (Exception e) {
			return true;
		}
	}

	@Override
	public void run() {
		if (!registerApp()) {
			System.exit(1);
		}
		// set Locale.
		String slang = config.getProperty("user.language");
		String scountry = config.getProperty("user.country");
		String svariant = config.getProperty("user.variant");
		if (slang != null && !slang.equals("") && scountry != null && svariant != null) {
			Locale.setDefault(new Locale(slang, scountry, svariant));
		}
		// Set the format patterns
		Formats.setIntegerPattern(config.getProperty("format.integer"));
		Formats.setDoublePattern(config.getProperty("format.double"));
		Formats.setCurrencyPattern(config.getProperty("format.currency"));
		Formats.setPercentPattern(config.getProperty("format.percent"));
		Formats.setDatePattern(config.getProperty("format.date"));
		Formats.setTimePattern(config.getProperty("format.time"));
		Formats.setDateTimePattern(config.getProperty("format.datetime"));
		// Set the look and feel.
		try {
			Object laf = Class.forName(config.getProperty("swing.defaultlaf")).newInstance();
			if (laf instanceof LookAndFeel) {
				UIManager.setLookAndFeel((LookAndFeel)laf);
			}
			else if (laf instanceof SubstanceSkin) {
				SubstanceLookAndFeel.setSkin((SubstanceSkin)laf);
			}
		}
		catch (Exception e) {
			logger.log(Level.WARNING, "Cannot set look and feel", e);
		}
		AppView appView = null;
		String screenmode = config.getProperty("machine.screenmode");
		if ("fullscreen".equals(screenmode)) {
			JRootKiosk rootkiosk = new JRootKiosk();
			appView = rootkiosk.initFrame(config);
		}
		else {
			JRootFrame rootframe = new JRootFrame();
			appView = rootframe.initFrame(config);
		}
		legacyFactory.initAppView(appView);
		monthyReports.startReporting();
		wifiService.initAppView(appView);
	}
}

package org.openpos.wifi;

public class WifiConfig {

	private String ssid;
	private String wpaKey;
	private String promotion;

	public WifiConfig(String ssid, String wpaKey, String promotion) {
		this.ssid = ssid;
		this.wpaKey = wpaKey;
		this.promotion = promotion;
	}

	public String getQrCode() {
		return "WIFI:S:" + ssid + ";T:WPA;P:" + wpaKey + ";;";
	}

	public String getSsid() {
		return ssid;
	}

	public String getWpaKey() {
		return wpaKey;
	}

	public String getPromotion() {
		return promotion;
	}
}

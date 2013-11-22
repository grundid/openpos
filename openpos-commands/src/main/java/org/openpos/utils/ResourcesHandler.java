package org.openpos.utils;

import java.io.UnsupportedEncodingException;

import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSystem;

public abstract class ResourcesHandler {

	protected DataLogicSystem dataLogicSystem;

	protected ResourcesHandler(AppView app) {
		dataLogicSystem = (DataLogicSystem)app.getBean("com.openbravo.pos.forms.DataLogicSystem");
	}

	protected String getResourceAsString(String key) {
		return dataLogicSystem.getResourceAsText(key);
	}

	protected void setResourceAsString(String key, String value) {
		try {
			dataLogicSystem.setResource(key, 0, value.getBytes("UTF8"));
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}

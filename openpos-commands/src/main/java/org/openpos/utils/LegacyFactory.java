package org.openpos.utils;

import org.springframework.stereotype.Service;

import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.IDataLogicSystem;

@Service
public class LegacyFactory {

	private AppView appView;

	public void initAppView(AppView appView) {
		this.appView = appView;
	}

	public AppView getAppView() {
		return appView;
	}

	public IDataLogicSystem getDataLogicSystem() {
		return (DataLogicSystem)appView.getBean("com.openbravo.pos.forms.DataLogicSystem");
	}
}

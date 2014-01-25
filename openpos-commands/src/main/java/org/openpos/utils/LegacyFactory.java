package org.openpos.utils;

import javax.swing.JFrame;

import org.springframework.stereotype.Service;

import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.IDataLogicSystem;

@Service
public class LegacyFactory {

	private AppView appView;
	private JFrame rootframe;

	public void initAppView(AppView appView, JFrame rootframe) {
		this.appView = appView;
		this.rootframe = rootframe;
	}

	public AppView getAppView() {
		return appView;
	}

	public JFrame getRootframe() {
		return rootframe;
	}

	public IDataLogicSystem getDataLogicSystem() {
		return (DataLogicSystem)appView.getBean("com.openbravo.pos.forms.DataLogicSystem");
	}
}

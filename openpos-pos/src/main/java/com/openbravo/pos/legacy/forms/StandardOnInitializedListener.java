package com.openbravo.pos.legacy.forms;

import java.sql.SQLException;

import javax.swing.JLabel;

import org.openpos.AppContext;
import org.openpos.events.OnInitializedListener;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Session;
import com.openbravo.pos.forms.AppProperties;
import com.openbravo.pos.forms.IDataLogicSystem;

public class StandardOnInitializedListener implements OnInitializedListener {

	private JLabel status;

	public StandardOnInitializedListener(JLabel status) {
		this.status = status;
	}

	@Override
	public void onInitialized(AppContext appContext) {
		IDataLogicSystem dataLogicSystem = appContext.getBean(IDataLogicSystem.class);
		Session session = appContext.getBean(Session.class);
		String m_sInventoryLocation = appContext.getBean(String.class, "inventoryLocation");
		AppProperties m_props = appContext.getBean(AppProperties.class);

		String sWareHouse;
		try {
			sWareHouse = dataLogicSystem.findLocationName(m_sInventoryLocation);
		}
		catch (BasicException e) {
			sWareHouse = null; // no he encontrado el almacen principal
		}

		// Show Hostname, Warehouse and URL in taskbar
		String url;
		try {
			url = session.getURL();
		}
		catch (SQLException e) {
			url = "";
		}
		status.setText("<html>" + m_props.getHost() + " - " + sWareHouse + "<br>" + url);

	}

}

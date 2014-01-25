package org.openpos.reports.cash;

import org.openpos.reports.CashFromToModel;
import org.openpos.reports.ICashFromToDialog;
import org.openpos.utils.LegacyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CashFromToService implements ICashFromToDialog {

	@Autowired
	private LegacyFactory legacyFactory;

	@Override
	public CashFromToModel getCashFromToModel() {
		CashFromToView view = new CashFromToView();
		if (ComponentDialog.showMessage(legacyFactory.getRootframe(), view)) {
			return view.getCashFromToModel();
		}
		else
			return null;
	}
}

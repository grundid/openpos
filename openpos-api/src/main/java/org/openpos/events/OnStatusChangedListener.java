package org.openpos.events;

import org.openpos.AppContext;

public interface OnStatusChangedListener {

	void onAppOpen(AppContext appContext);

	void onAppClose(AppContext appContext);
}

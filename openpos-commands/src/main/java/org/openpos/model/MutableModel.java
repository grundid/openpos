package org.openpos.model;

import java.util.ArrayList;
import java.util.Collection;

public abstract class MutableModel {

	private Collection<OnModelChangedListener> listenerList = new ArrayList<OnModelChangedListener>();

	public void addOnModelChangedListener(OnModelChangedListener listener) {
		listenerList.add(listener);
	}

	protected void notifyModelChangedListeners() {
		for (OnModelChangedListener listener : listenerList) {
			listener.onModelChanged(this);
		}
	}
}

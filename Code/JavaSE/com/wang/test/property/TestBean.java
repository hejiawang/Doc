package com.wang.test.property;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class TestBean {

	protected final PropertyChangeSupport support = new PropertyChangeSupport(this);
	
	private String str;
	
	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		support.firePropertyChange("str", this.str, str);
		this.str = str;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
}

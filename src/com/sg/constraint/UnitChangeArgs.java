package com.sg.constraint;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import com.sg.unit.BaseUnit;
import com.sg.unit.SGEventObject;

public class UnitChangeArgs extends EventObject {

	private static final long serialVersionUID = 1L;
	private float preValue;
	private float preValue2;
	private List<SGEventObject> unitsPass;
	private boolean handled;
	
	public UnitChangeArgs(SGEventObject source, float preValue, float preValue2) {
		super(source);
		unitsPass = new ArrayList<SGEventObject>();
		unitsPass.add(source);
		this.setPreValue(preValue);
		this.setPreValue2(preValue2);
		this.setHandled(false);
	}
	
	public UnitChangeArgs Next(SGEventObject cur) {
		unitsPass.add(cur);
		return this;
	}
	
	public boolean constains(SGEventObject u) {
		return unitsPass.contains(u);
	}
	
	public SGEventObject getSource() {
		return unitsPass.get(unitsPass.size() - 1);
	}
	
	public SGEventObject getOrginSource() {
		return unitsPass.get(0);
	}

	public float getPreValue() {
		return preValue;
	}

	public void setPreValue(float preValue) {
		this.preValue = preValue;
	}

	public float getPreValue2() {
		return preValue2;
	}

	public void setPreValue2(float preValue2) {
		this.preValue2 = preValue2;
	}

	public boolean isHandled() {
		return handled;
	}

	public void setHandled(boolean handled) {
		this.handled = handled;
	}

	public List<SGEventObject> getUnitsPass() {
		return unitsPass;
	}

}

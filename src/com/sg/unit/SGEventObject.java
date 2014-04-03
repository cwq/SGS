package com.sg.unit;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.sg.constraint.IChangable;
import com.sg.constraint.UnitChangeArgs;

public abstract class SGEventObject {

	protected Set<IChangable> listener;
	
	public SGEventObject() {
		// TODO Auto-generated constructor stub
		this.listener = new HashSet<IChangable>();
	}
	
	public void addUnitListener(IChangable cst) {
		this.listener.add(cst);
	}
	
	protected void notifies(UnitChangeArgs e) {
		IChangable cst = null;
		Iterator<IChangable> iterator = this.listener.iterator();
		while (iterator.hasNext()) {
			cst = iterator.next();
			cst.OnChange(e);
		}
	}
}

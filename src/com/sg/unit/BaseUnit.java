package com.sg.unit;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.graphics.Canvas;
import android.util.Log;

import com.sg.constraint.*;
import com.sg.property.common.Point;
import com.sg.property.tools.Painter;

public abstract class BaseUnit {
	static private long sid = Long.MIN_VALUE;
	
	private long id;
	protected Set<IChangable> listener;
	
	protected BaseUnit() {
		this.id = sid++;
		this.listener = new HashSet<IChangable>();
	}
	
	public long getID() {
		return id;
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
	
	public abstract void draw(Canvas canvas, Painter painter);
	
	public abstract boolean isInUnit(Point point);
	
	public abstract void translate(Point vector);
	
	public abstract void scale(Point vector);
	
	/**
	 * cosQ, -sinQ 
	 * sinQ, cosQ
	 * @param vector
	 * x = sinQ, y = cosQ
	 */
	public abstract void rotate(double rotateAngle);
}

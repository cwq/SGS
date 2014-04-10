package com.sg.object;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.graphics.Canvas;

import com.sg.constraint.IChangable;
import com.sg.constraint.UnitChangeArgs;
import com.sg.property.tools.Painter;

public abstract class SGObject {
	
	static private long sid = Long.MIN_VALUE;
	
	private long id;

	protected Set<IChangable> listener;
	
	public SGObject() {
		// TODO Auto-generated constructor stub
		this.id = sid++;
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
	
	public long getID() {
		return id;
	}
	
	public boolean contains(SGObject object) {
		return object.id == this.id;
	}
	
	public abstract void draw(Canvas canvas, Painter painter);
	
	public abstract boolean isInObject(Point point);
	
	public abstract void translate(Point vector);
	
	public abstract void scale(Point vector);
	
	public abstract void rotate(double rotateAngle);
}

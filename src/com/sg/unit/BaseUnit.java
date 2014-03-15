package com.sg.unit;

import android.graphics.Canvas;

import com.sg.property.common.Point;
import com.sg.property.tools.Painter;

public abstract class BaseUnit {
	static private long sid = Long.MIN_VALUE;
	private long id;
	
	protected BaseUnit() {
		id = sid++;
	}
	
	public long getID() {
		return id;
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

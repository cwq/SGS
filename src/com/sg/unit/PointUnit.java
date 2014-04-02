package com.sg.unit;

import android.R.string;
import android.graphics.Canvas;

import com.sg.constraint.ConstraintHandler;
import com.sg.constraint.UnitChangeArgs;
import com.sg.property.common.CommonFunction;
import com.sg.property.common.Point;
import com.sg.property.common.ThresholdProperty;
import com.sg.property.tools.Painter;

public class PointUnit extends BaseUnit {
	
	private float X;
	private float Y;
	
	public PointUnit() {
		X = Y = 0;
	}

	public PointUnit(PointUnit p) {
		this.Set(p);
	}
	public PointUnit(Point p) {
		this.Set(p);
	}
	
	public void Set(PointUnit p) {
		this.setX(p.getX());
		this.setY(p.getY());
	}	
	public void Set(Point p) {
		this.setX(p.getX());
		this.setY(p.getY());
	}

	public float getX() {
		return X;
	}

	public void setX(float x) {
		this.Set(x, this.Y);
	}

	public float getY() {
		return Y;
	}

	public void setY(float y) {
		this.Set(this.X, y);
	}
	
	public Point toPoint() {
		return new Point(X, Y);
	}
	
	public void Set(float x, float y) {
		Set(x, y, null);
	}
	
	public void Set(double x, double y) {
		Set((float)x, (float)y);
	}
	
	public void Set(float x, float y, UnitChangeArgs e) {
		if(Math.abs(x - this.X) < ThresholdProperty.FLOAT_OFFSET
				&& Math.abs(y - this.Y) < ThresholdProperty.FLOAT_OFFSET) {
			return;
		}
		
		if (e != null && e.constains(this)) return;
		if (e == null) e = new UnitChangeArgs(this, this.X, this.Y);
		if (e.isHandled()) return;
		
		this.X = x;
		this.Y = y;
		
		notifies(e);
	}
	
	@Override
	public String toString() {
		return "Point(" + this.getID() + ") " + this.X + ", " + this.Y;
	}

	@Override
	public void draw(Canvas canvas, Painter painter) {
		// TODO Auto-generated method stub
		canvas.drawCircle(this.X, this.Y, painter.getWidth()/2, painter.getPaint());
		canvas.drawCircle(this.X, this.Y, painter.getWidth()/4, painter.getPaint());
	}

	@Override
	public boolean isInUnit(Point point) {
		// TODO Auto-generated method stub
		double curDistance = CommonFunction.distance(new Point(X, Y), point);
		if(curDistance < ThresholdProperty.GRAPH_CHECKED_DISTANCE){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void translate(Point vector) {
		// TODO Auto-generated method stub
		this.Set(this.getX() + vector.getX(), this.getY() + vector.getY());
	}

	@Override
	public void scale(Point vector) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotate(double rotateAngle) {
		// TODO Auto-generated method stub
		
	}	
}

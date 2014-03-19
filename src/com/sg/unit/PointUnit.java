package com.sg.unit;

import android.graphics.Canvas;

import com.sg.constraint.ConstraintHandler;
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
		X = x;
		if (ConstraintHandler.IsInUcKeys(this))
			ConstraintHandler.Changed(this);
	}

	public float getY() {
		return Y;
	}

	public void setY(float y) {
		Y = y;
		if (ConstraintHandler.IsInUcKeys(this))
			ConstraintHandler.Changed(this);
	}
	
	public Point getPoint() {
		return new Point(X, Y);
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

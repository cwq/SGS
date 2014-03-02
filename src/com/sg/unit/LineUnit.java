package com.sg.unit;

import android.graphics.Canvas;

import com.sg.property.common.CommonFunction;
import com.sg.property.common.Point;
import com.sg.property.common.ThresholdProperty;
import com.sg.property.tools.Painter;

public class LineUnit extends BaseUnit {
	
	private PointUnit end1;
	private PointUnit end2;
	
	public LineUnit(Point p1, Point p2) {
		end1 = new PointUnit(p1);
		end2 = new PointUnit(p2);
	}
	
	public PointUnit getEnd1() {
		return end1;
	}
	public void setEnd1(PointUnit end1) {
		this.end1.Set(end1);
	}
	public PointUnit getEnd2() {
		return end2;
	}
	public void setEnd2(PointUnit end2) {
		this.end2.Set(end2);
	}
	@Override
	public void draw(Canvas canvas, Painter painter) {
		// TODO Auto-generated method stub
		end1.draw(canvas, painter);
		end2.draw(canvas, painter);
		canvas.drawLine(end1.getX(), end1.getY(), end2.getX(), end2.getY(), painter.getPaint());
	}
	@Override
	public boolean isInUnit(Point point) {
		// TODO Auto-generated method stub
		double checkdistance1 = CommonFunction.distance(end1.getPoint(), point);
		double checkdistance2 = CommonFunction.distance(end2.getPoint(), point);
		double linedistance = CommonFunction.distance(end1.getPoint(), end2.getPoint());
		double curDistance = CommonFunction.pointToLineDistance(end1.getPoint(), end2.getPoint(), point);
		if(checkdistance1 < linedistance && checkdistance2 < linedistance && curDistance < ThresholdProperty.GRAPH_CHECKED_DISTANCE){
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
	public void rotate(Point vector) {
		// TODO Auto-generated method stub
		
	}	
}

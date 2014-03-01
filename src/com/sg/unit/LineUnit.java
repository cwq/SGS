package com.sg.unit;

import android.graphics.Canvas;

import com.sg.property.common.Point;
import com.sg.property.tools.Painter;

public class LineUnit extends BaseUnit {
	
	private PointUnit end1;
	private PointUnit end2;
	
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
		
	}
	@Override
	public boolean isInUnit(Point point) {
		// TODO Auto-generated method stub
		return false;
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

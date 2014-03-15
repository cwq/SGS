package com.sg.property.common;

public class Point {

	private float x;
	private float y;
	
	public Point() {
		this(0, 0);
	}
	
	public Point(double x, double y) {
		this((float)x, (float)y);
	}
	
	public Point(float x, float y) {
		this.setX(x);
		this.setY(y);
	}
	
	public Point(Point p) {
		this(p.getX(), p.getY());
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
}

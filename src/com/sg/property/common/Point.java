package com.sg.property.common;

public class Point {

	private float x;
	private float y;
	
	private double speed;        //速率
	private double direction;    //方向
	private double curvity;     //曲率
	
	private int total;          //特征点权重值，根据速率，方向，曲率来计算
	
	public Point() {
		this(0, 0);
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
	
	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

	public double getCurvity() {
		return curvity;
	}

	public void setCurvity(double curvity) {
		this.curvity = curvity;
	}

	public int getTotal() {
		return total;
	}
	
	/*
	 * 增指定值
	 * */
	public void increaseTotal(int increaseNum) {
		this.total += increaseNum;
	}
	
}

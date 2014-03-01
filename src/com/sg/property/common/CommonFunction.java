package com.sg.property.common;

public class CommonFunction {
	public static double distance(Point p1, Point p2) {
		return Math.sqrt((p1.getX() - p2.getX())*(p1.getX() - p2.getX())+
				(p1.getY() - p2.getY())*(p1.getY() - p2.getY()));
	}
}

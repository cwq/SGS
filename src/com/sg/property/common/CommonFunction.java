package com.sg.property.common;

public class CommonFunction {
	
	/**
	 * 两点间距离
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double distance(Point p1, Point p2) {
		return Math.sqrt((p1.getX() - p2.getX())*(p1.getX() - p2.getX())+
				(p1.getY() - p2.getY())*(p1.getY() - p2.getY()));
	}
	
	/**
	 * curPoint到直线（startpoint，endpoint）的距离
	 * @param startpoint
	 * @param endpoint
	 * @param curPoint
	 * @return curPoint到直线（startpoint，endpoint）的距离
	 */
	public static double pointToLineDistance(Point startpoint,Point endpoint,Point curPoint){
		double x1,x2,y1,y2,curX,curY;
		x1=(double)startpoint.getX();
		x2=(double)endpoint.getX();
		y1=(double)startpoint.getY();
		y2=(double)endpoint.getY();
		curX=(double)curPoint.getX();
		curY=(double)curPoint.getY();
		double distance;
		if(x1 == x2)
			distance = Math.abs(curX - x1);
		else
			distance=Math.abs((y1-y2)/(x1-x2)*curX-curY-(y1-y2)/(x1-x2)*x1+y1)/Math.sqrt(((y1-y2)*(y1-y2))/((x1-x2)*(x1-x2))+1);
		return distance;
	}
}

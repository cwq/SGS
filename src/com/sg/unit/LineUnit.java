package com.sg.unit;

import java.util.List;

import android.graphics.Canvas;

import com.sg.property.common.CommonFunction;
import com.sg.property.common.Point;
import com.sg.property.common.ThresholdProperty;
import com.sg.property.tools.Painter;

public class LineUnit extends BaseUnit {
	
	private PointUnit end1;
	private PointUnit end2;
	
	public LineUnit() {
		end1 = new PointUnit();
		end2 = new PointUnit();
	}
	
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
	
	/**
	 * 判断是否是直线图元的方法：若首末两点的距离比上所有的两两相邻的点之间的距离之和，比之大于阀值的话，则判断为直线图元
     * 阀值暂定为0.98
	 * @param pList
	 * @return
	 */
	public boolean Adapt(List<Point> pList) {
		int n = pList.size();
		if (n < 2) {
			return false;
		}
        //判断是否是直线图元的方法：若首末两点的距离比上所有的两两相邻的点之间的距离之和，比之大于阀值的话，则判断为直线图元
        //阀值暂定为0.95
		double totalLength = 0.0;
		double tmpLength = CommonFunction.distance(pList.get(0), pList.get(n-1));
		
		if (tmpLength < ThresholdProperty.TWO_POINT_IS_CONSTRAINTED) {
			return false;
		}
		for(int i = 0; i < n-1; i++) {
			totalLength += CommonFunction.distance(pList.get(i), pList.get(i+1));
		}
		
		if(tmpLength / totalLength >= ThresholdProperty.JUDGE_LINE_VALUE) {
			return true;
		}
		
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
	public void rotate(double rotateAngle) {
		// TODO Auto-generated method stub
		
	}	
}

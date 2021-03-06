package com.sg.unit;

import java.util.List;

import android.graphics.Canvas;

import com.sg.constraint.IChangable;
import com.sg.constraint.UnitChangeArgs;
import com.sg.object.Point;
import com.sg.object.SGObject;
import com.sg.property.common.CommonFunction;
import com.sg.property.common.ThresholdProperty;
import com.sg.property.tools.Painter;

public class LineUnit extends BaseUnit implements IChangable {
	
	private PointUnit end1;
	private PointUnit end2;
	
	public LineUnit() {
		this(new Point(), new Point());
	}
	
	public LineUnit(Point p1, Point p2) {
		this.end1 = new PointUnit(p1);
		this.end2 = new PointUnit(p2);
		this.end1.addUnitListener(this);
		this.end2.addUnitListener(this);
		//端点的groupid和线元相同
		end1.setGroup(this.getGroup());
		end2.setGroup(this.getGroup());
	}
	
	@Override
	public void setGroup(long group) {
		// TODO Auto-generated method stub
		super.setGroup(group);
		end1.setGroup(group);
		end2.setGroup(group);
	}
	
	public void OnChange(UnitChangeArgs e) {
		notifies(e.Next(this));
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
	
	public boolean contains(SGObject object) {
		return super.contains(object)
				|| end1.contains(object) || end2.contains(object);
	}
	
	@Override
	public void draw(Canvas canvas, Painter painter) {
		// TODO Auto-generated method stub
		end1.draw(canvas, painter);
		end2.draw(canvas, painter);
		canvas.drawLine(end1.getX(), end1.getY(), end2.getX(), end2.getY(), painter.getPaint());
	}
	
	@Override
	public boolean isInObject(Point point) {
		// TODO Auto-generated method stub
		if (end1.isInObject(point) || end2.isInObject(point)) {
			return true;
		}
		double checkdistance1 = CommonFunction.distance(end1.toPoint(), point);
		double checkdistance2 = CommonFunction.distance(end2.toPoint(), point);
		double linedistance = CommonFunction.distance(end1.toPoint(), end2.toPoint());
		double curDistance = CommonFunction.pointToLineDistance(end1.toPoint(), end2.toPoint(), point);
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
	public String toString() {
		return "Line(" + this.getID() +")";
	}
	
	@Override
	public void translate(Point vector, UnitChangeArgs e) {
		// TODO Auto-generated method stub
		end1.translate(vector, e);
		end2.translate(vector, e);
	}
	
	@Override
	public void scale(Point vector, UnitChangeArgs e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void rotate(double rotateAngle, UnitChangeArgs e) {
		// TODO Auto-generated method stub
		
	}	
}

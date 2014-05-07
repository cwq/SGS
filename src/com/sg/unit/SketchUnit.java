package com.sg.unit;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Canvas;

import com.sg.constraint.UnitChangeArgs;
import com.sg.object.Point;
import com.sg.property.common.CommonFunction;
import com.sg.property.common.ThresholdProperty;
import com.sg.property.tools.Painter;

public class SketchUnit extends BaseUnit {
	
	private List<Point> pList;
	
	public SketchUnit() {
		pList = new ArrayList<Point>();
	}
	
	public SketchUnit(List<Point> points) {
		pList = points;
	}
	
	public void addPoint(Point p) {
		pList.add(p);
	}
	
	public synchronized void setPointList(List<Point> points){
			pList = points;
	}
	
	public List<Point> getPoints() {
		return pList;
	}
	
	public synchronized void clear() {
			pList.clear();
	}

	@Override
	public synchronized void draw(Canvas canvas, Painter painter) {
		// TODO Auto-generated method stub
//		synchronized(pList) {
			if(pList == null)
				return;
			int n = pList.size();
			for(int i = 0; i < n-1; i++) {
				if(pList.size()-1 > i)
					canvas.drawLine(pList.get(i).getX(), pList.get(i).getY(), pList.get(i+1).getX(), pList.get(i+1).getY(), painter.getPaint());			
			}
//		}
	}

	@Override
	public boolean isInObject(Point point) {
		// TODO Auto-generated method stub
		double curDistance;
		for(Point pt : pList) {
			curDistance = CommonFunction.distance(pt, point);
			if(curDistance < ThresholdProperty.GRAPH_CHECKED_DISTANCE) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void translate(Point vector, UnitChangeArgs e) {
		// TODO Auto-generated method stub
		for(Point pt : pList) {
			pt.setX(pt.getX() + vector.getX());
			pt.setY(pt.getY() + vector.getY());
		}
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

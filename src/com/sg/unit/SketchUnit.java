package com.sg.unit;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Canvas;

import com.sg.property.common.CommonFunction;
import com.sg.property.common.Point;
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
	
	public void setPointList(List<Point> points){
		synchronized(pList) {
			pList = points;
		}
	}
	
	public List<Point> getPoints() {
		return pList;
	}
	
	public void clear() {
		synchronized(pList) {
			pList.clear();
		}
	}

	@Override
	public void draw(Canvas canvas, Painter painter) {
		// TODO Auto-generated method stub
		if(pList == null)
			return;
		int n = pList.size();
		for(int i = 0; i < n-1; i++) {
			synchronized(pList) {
				if(pList.size()-1 > i)
					canvas.drawLine(pList.get(i).getX(), pList.get(i).getY(), pList.get(i+1).getX(), pList.get(i+1).getY(), painter.getPaint());
			}
			
		}
	}

	@Override
	public boolean isInUnit(Point point) {
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

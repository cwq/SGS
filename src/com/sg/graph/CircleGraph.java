package com.sg.graph;

import com.sg.constraint.IChangable;
import com.sg.constraint.UnitChangeArgs;
import com.sg.object.Point;
import com.sg.object.SGObject;
import com.sg.unit.*;

public class CircleGraph extends BaseGraph implements IChangable{
	
	private CurveUnit curveUnit;
	
	public CircleGraph(CurveUnit curveUnit) {
		this.curveUnit = curveUnit;
		setKey(String.valueOf(curveUnit.getID()));
		setGroup(curveUnit.getGroup());
	}
	
	public PointUnit getCenter() {
		return curveUnit.getCenter();
	}
	
	public void setCenter(Point p) {
		curveUnit.getCenter().Set(p);
	}
	
	public double getRadius() {
		return curveUnit.getA();
	}
	
	public void setRadius(double radius) {
		curveUnit.setAB(radius, radius);
	}
	
	public boolean contains(SGObject object) {
		return super.contains(object)
				|| curveUnit.contains(object);
	}

	@Override
	public void OnChange(UnitChangeArgs e) {
		// TODO Auto-generated method stub
		notifies(e.Next(this));
	}
}

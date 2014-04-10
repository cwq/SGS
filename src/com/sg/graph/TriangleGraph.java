package com.sg.graph;

import com.sg.constraint.CstPointsSamePos;
import com.sg.constraint.IChangable;
import com.sg.constraint.UnitChangeArgs;
import com.sg.object.SGObject;
import com.sg.property.common.CommonFunction;
import com.sg.unit.LineUnit;
import com.sg.unit.PointUnit;

public class TriangleGraph extends BaseGraph implements IChangable{

	private LineUnit edge1;
	private LineUnit edge2;
	private LineUnit edge3;
	private PointUnit point1;
	private PointUnit point2;
	private PointUnit point3;
	
	public TriangleGraph(LineUnit l1, LineUnit l2, LineUnit l3) {
		edge1 = l1;
		edge2 = l2;
		edge3 = l3;
		point1 = edge1.getEnd1();
		point2 = edge1.getEnd2();
		double d1 = Math.min(
				CommonFunction.distance(l2.getEnd1().toPoint(), l1.getEnd1().toPoint()), 
				CommonFunction.distance(l2.getEnd1().toPoint(), l1.getEnd2().toPoint()));
		double d2 = Math.min(
				CommonFunction.distance(l2.getEnd2().toPoint(), l1.getEnd1().toPoint()), 
				CommonFunction.distance(l2.getEnd2().toPoint(), l1.getEnd2().toPoint()));
		if (d1 < d2) point3 = l2.getEnd2();
		else point3 = l2.getEnd1();

		CstPointsSamePos.Add(point1, nearestPoint(point1, edge2, edge3));
		CstPointsSamePos.Add(point2, nearestPoint(point2, edge2, edge3));
		CstPointsSamePos.Add(point3, nearestPoint(point1, edge1, edge3));
		
		edge1.addUnitListener(this);
		edge2.addUnitListener(this);
		edge3.addUnitListener(this);
	}
	
	private PointUnit nearestPoint(PointUnit p, LineUnit l1, LineUnit l2) {
		PointUnit res = l1.getEnd1();
		if (CommonFunction.distance(l1.getEnd2().toPoint(), p.toPoint())
				< CommonFunction.distance(res.toPoint(), p.toPoint()))
			res = l1.getEnd2();
		if (CommonFunction.distance(l2.getEnd1().toPoint(), p.toPoint())
				< CommonFunction.distance(res.toPoint(), p.toPoint()))
			res = l2.getEnd1();
		if (CommonFunction.distance(l2.getEnd2().toPoint(), p.toPoint())
				< CommonFunction.distance(res.toPoint(), p.toPoint()))
			res = l2.getEnd2();
		return res;
	}
	
	@Override
	public void OnChange(UnitChangeArgs e) {
		// TODO Auto-generated method stub
		notifies(e.Next(this));
	}
	
	public boolean contains(SGObject object) {
		return super.contains(object)
				|| edge1.contains(object)
				|| edge2.contains(object)
				|| edge3.contains(object);
	}

	public void setPoint1(PointUnit point1) {
		this.point1.Set(point1);
	}

	public void setPoint2(PointUnit point2) {
		this.point2.Set(point2);
	}

	public void setPoint3(PointUnit point3) {
		this.point3.Set(point3);
	}

	public LineUnit getEdge1() {
		return edge1;
	}

	public LineUnit getEdge2() {
		return edge2;
	}

	public LineUnit getEdge3() {
		return edge3;
	}

	public PointUnit getPoint1() {
		return point1;
	}

	public PointUnit getPoint2() {
		return point2;
	}

	public PointUnit getPoint3() {
		return point3;
	}
	
}

package com.sg.constraint;

import com.sg.graph.CircleGraph;
import com.sg.graph.TriangleGraph;

public class CstTriangleInCircle extends BaseConstraint {

	public static void Add(CircleGraph c, TriangleGraph t) {
		CstPointOnCircle.Add(c, t.getPoint1());
		CstPointOnCircle.Add(c, t.getPoint2());
		CstPointOnCircle.Add(c, t.getPoint3());
	}
	
	@Override
	public void OnChange(UnitChangeArgs e) {
		// TODO Auto-generated method stub
		
	}
	
}

package com.sg.constraint;

import java.util.HashMap;

import com.sg.graph.CircleGraph;
import com.sg.graph.TriangleGraph;
import com.sg.object.SGObject;

public class CstTriangleInCircle extends BaseConstraint {
	
	static private HashMap<TriangleGraph, CircleGraph> cstMap
			 = new HashMap<TriangleGraph, CircleGraph>();

	public static void Add(CircleGraph c, TriangleGraph t) {
		if (cstMap.containsKey(t)) return;
		cstMap.put(t, c);
		CstPointOnCircle.Add(c, t.getPoint1());
		CstPointOnCircle.Add(c, t.getPoint2());
		CstPointOnCircle.Add(c, t.getPoint3());
	}

	public static boolean isRelated(SGObject o1, SGObject o2) {
		for (TriangleGraph itt : cstMap.keySet()) {
			if ((itt.contains(o1) && cstMap.get(itt).contains(o2))
					|| (itt.contains(o2) && cstMap.get(itt).contains(o1)))
				return true;
		}
		return false;
	}
	
	@Override
	public void OnChange(UnitChangeArgs e) {
		// TODO Auto-generated method stub
		
	}
	
}

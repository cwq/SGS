package com.sg.constraint;

import java.util.HashMap;
import com.sg.graph.*;
import com.sg.object.Point;
import com.sg.object.SGObject;
import com.sg.property.common.CommonFunction;

public class CstCircleInTriangle extends BaseConstraint {
	
	private static CstCircleInTriangle instance
			= new CstCircleInTriangle();
	public static CstCircleInTriangle getInstance() {
		return instance;
	}
	private CstCircleInTriangle() {
		// TODO Auto-generated constructor stub
	}
	
	public static HashMap<CircleGraph, TriangleGraph> cstMap
			= new HashMap<CircleGraph, TriangleGraph>();

    public static void Add(CircleGraph c, TriangleGraph t)
    {
        if (cstMap.containsKey(c)) return;
        cstMap.put(c, t);
        c.addUnitListener(CstCircleInTriangle.getInstance());
        t.addUnitListener(CstCircleInTriangle.getInstance());
        
        ConstraintHandler.union(c.getGroup(), t.getGroup());
    }

	public static boolean isRelated(SGObject o1, SGObject o2) {
		for (CircleGraph itc : cstMap.keySet()) {
			if ((itc.contains(o1) && cstMap.get(itc).contains(o2))
					|| (itc.contains(o2) && cstMap.get(itc).contains(o1)))
				return true;
		}
		return false;
	}

    public void OnChange(UnitChangeArgs e)
    {
        CircleGraph c = null;
        TriangleGraph t = null;
        if (e.getSource() instanceof CircleGraph) {
        	c = (CircleGraph)e.getSource();
        	t = cstMap.get(c);
        } else if (e.getSource() instanceof TriangleGraph) {
        	t = (TriangleGraph)e.getSource();
        	for (CircleGraph tc : cstMap.keySet()) {
				if (cstMap.get(tc) == t) {
					c = tc; break;
				}
			}
		}
        if (c == null || t == null) return;

        double la = CommonFunction.distance(t.getPoint1().toPoint(), t.getPoint2().toPoint());
        double lb = CommonFunction.distance(t.getPoint2().toPoint(), t.getPoint3().toPoint());
        double lc = CommonFunction.distance(t.getPoint3().toPoint(), t.getPoint1().toPoint());
        c.getCenter().Set(new Point(
            (la * t.getPoint3().getX() + lb * t.getPoint1().getX() + lc * t.getPoint2().getX()) / (la + lb + lc),
            (la * t.getPoint3().getY() + lb * t.getPoint1().getY() + lc * t.getPoint2().getY()) / (la + lb + lc)));

        double p = (la + lb + lc) / 2;
        double S = Math.sqrt(p * (p - la) * (p - lb) * (p - lc));
        S = 2 * S / (la + lb + lc);
        c.setRadius(S);
    }
}

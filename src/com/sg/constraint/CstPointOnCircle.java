package com.sg.constraint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.sg.graph.CircleGraph;
import com.sg.property.common.CommonFunction;
import com.sg.property.common.Point;
import com.sg.unit.PointUnit;

public class CstPointOnCircle extends BaseConstraint {
	
	private static CstPointOnCircle instance = new CstPointOnCircle();
	public static CstPointOnCircle getInstance() {
		return instance;
	}
	
	private CstPointOnCircle() {
	}

	private static HashMap<CircleGraph, List<PointUnit>> cstMap
			= new HashMap<CircleGraph, List<PointUnit>>();
	private static HashMap<PointUnit, Double> cstPreAngle 
			= new HashMap<PointUnit, Double>();

	public static void Add(CircleGraph c, PointUnit p) {
		Iterator<List<PointUnit>> vit = cstMap.values().iterator();
		List<PointUnit> pList;
		while (vit.hasNext()) {
			pList = vit.next();
			if (pList.contains(p))
				return;
		}
		
		if (cstMap.containsKey(c) == false) 
			cstMap.put(c, new ArrayList<PointUnit>());
		cstMap.get(c).add(p);
		cstPreAngle.put(p, CommonFunction.VectorToAngle(
				p.toPoint(), c.getCenter().toPoint()));
		
		c.addUnitListener(CstPointOnCircle.getInstance());
		p.addUnitListener(CstPointOnCircle.getInstance());
	}

	public void OnChange(UnitChangeArgs e) {
		
		CircleGraph c = null;
        if (e.getSource() instanceof CircleGraph)
        {
            c = (CircleGraph)e.getSource();
            for (PointUnit p : cstMap.get(c))
            {
                Point tp = CommonFunction.RotatePoint(new Point(c.getCenter().getX() + c.getRadius(), c.getCenter().getY()),
                		cstPreAngle.get(p), c.getCenter().toPoint());
                p.Set(tp.getX(), tp.getY());
            }
        }
        else if (e.getOrginSource() instanceof PointUnit)
        {
            PointUnit p = (PointUnit)e.getOrginSource();

            for (CircleGraph tc : cstMap.keySet())
                if (cstMap.get(tc).contains(p))
                { c = tc; break; }
            if (c == null) return;

            Point tp = CommonFunction.RotatePoint(new Point(c.getCenter().getX() + c.getRadius(), c.getCenter().getY()),
                CommonFunction.VectorToAngle(p.toPoint(), c.getCenter().toPoint()), c.getCenter().toPoint());

            if (CommonFunction.distance(p.toPoint(), c.getCenter().toPoint()) > 5)   // 防止缩成一点，造成计算角度错误
                cstPreAngle.put(p, CommonFunction.VectorToAngle(p.toPoint(), c.getCenter().toPoint()));

            e.setHandled(true);
            p.Set(tp.getX(), tp.getY());
        }
        else
        {
            return;
        }
	}
	
}

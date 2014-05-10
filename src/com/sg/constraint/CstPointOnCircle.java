package com.sg.constraint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.sg.graph.CircleGraph;
import com.sg.object.Point;
import com.sg.object.SGObject;
import com.sg.property.common.CommonFunction;
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
		
		// p原来就与其他点有约束
		if (CstPointsSamePos.cstMap.get(p) != null) {
			for (PointUnit pointUnit : CstPointsSamePos.cstMap.get(p)) {
				cstMap.get(c).add(pointUnit);
				cstPreAngle.put(pointUnit, CommonFunction.VectorToAngle(
						pointUnit.toPoint(), c.getCenter().toPoint()));
				pointUnit.addUnitListener(CstPointOnCircle.getInstance());
			}
		}
		
		c.addUnitListener(CstPointOnCircle.getInstance());
		p.addUnitListener(CstPointOnCircle.getInstance());
		
		ConstraintHandler.union(c.getGroup(), p.getGroup());
		//添加约束后 马上更新点的位置
		Point tp = CommonFunction.RotatePoint(new Point(c.getCenter().getX() + c.getRadius(), c.getCenter().getY()),
				cstPreAngle.get(p), c.getCenter().toPoint());
		p.Set(tp.getX(), tp.getY());
	}

	public static boolean isRelated(SGObject o1, SGObject o2) {
		for (CircleGraph itc : cstMap.keySet()) {
			if (itc.contains(o1)) {
				for (PointUnit itp : cstMap.get(itc)) {
					if (o2.contains(itp)) return true;
				}
				break;
			} else if (itc.contains(o2)) {
				for (PointUnit itp : cstMap.get(itc)) {
					if (o1.contains(itp)) return true;
				}
				break;
			}
		}
		return false;
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

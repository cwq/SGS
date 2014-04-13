package com.sg.constraint;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.sg.object.SGObject;
import com.sg.unit.*;

public class CstPointsSamePos extends BaseConstraint {
	
	private static CstPointsSamePos instance = new CstPointsSamePos();
	public static CstPointsSamePos getInstance() {
		return instance;
	}
	
	private CstPointsSamePos() {
	}
	
	public static HashMap<PointUnit, Set<PointUnit>> cstMap
			= new HashMap<PointUnit, Set<PointUnit>>();

	public static void Add(PointUnit a, PointUnit b) {
		if (cstMap.get(a) == null) cstMap.put(a, new HashSet<PointUnit>());
		if (cstMap.get(b) == null) cstMap.put(b, new HashSet<PointUnit>());
		// a原来就与其他点有约束
		if (!cstMap.get(a).isEmpty()) {
			cstMap.get(b).addAll(cstMap.get(a));
			for (PointUnit pUnit : cstMap.get(a)) {
				cstMap.get(pUnit).add(b);
			}
		}
		cstMap.get(a).add(b);
		cstMap.get(b).add(a);
		a.addUnitListener(CstPointsSamePos.getInstance());
		b.addUnitListener(CstPointsSamePos.getInstance());
	}

	public static boolean isRelated(SGObject o1, SGObject o2) {
		for (PointUnit itp : cstMap.keySet()) {
			if (o1.contains(itp)) {
				for (PointUnit itp2 : cstMap.get(itp)) {
					if (o2.contains(itp2)) return true;
				}
			}
		}
		return false;
	}

	public void OnChange(UnitChangeArgs e) {
		
		if (!(e.getOrginSource() instanceof PointUnit)) return;
		
		PointUnit s = (PointUnit)e.getOrginSource();
		for (PointUnit p : cstMap.get(s)) {
			p.Set(s.getX(), s.getY(), e);
		}
	}
	
}

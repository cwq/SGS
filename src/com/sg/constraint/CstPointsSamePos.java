package com.sg.constraint;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.sg.unit.*;

public class CstPointsSamePos extends BaseConstraint implements IChangable {
	
	private static CstPointsSamePos instance = new CstPointsSamePos();
	public static CstPointsSamePos getInstance() {
		return instance;
	}
	
	private CstPointsSamePos() {
	}
	
	public static Dictionary<PointUnit, List<PointUnit>> cstMap
			= new Hashtable<PointUnit, List<PointUnit>>();

	public static void Add(PointUnit a, PointUnit b) {
		if (cstMap.get(a) == null) cstMap.put(a, new ArrayList<PointUnit>());
		if (cstMap.get(b) == null) cstMap.put(b, new ArrayList<PointUnit>());
		cstMap.get(a).add(b);
		cstMap.get(b).add(a);
		a.addUnitListener(CstPointsSamePos.getInstance());
		b.addUnitListener(CstPointsSamePos.getInstance());
	}

	public void OnChange(UnitChangeArgs e) {
		
		if (!(e.getOrginSource() instanceof PointUnit)) return;
		
		PointUnit s = (PointUnit)e.getOrginSource();
		for (PointUnit p : cstMap.get(s)) {
			p.Set(s.getX(), s.getY(), e);
		}
	}
	
}

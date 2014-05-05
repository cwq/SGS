package com.sg.constraint;
import java.util.*;

import com.sg.controller.UnitController;
import com.sg.graph.*;
import com.sg.object.SGObject;
import com.sg.property.common.CommonFunction;
import com.sg.property.common.ThresholdProperty;
import com.sg.unit.*;


public class ConstraintHandler {

	/**
	 * 识别约束
	 * @param u
	 */
	static public void constraintRecognize(SGObject u) {
		if (u == null) {
			return;
		}
		if (u instanceof LineUnit) {
			LineUnit curLine = (LineUnit) u;
			for (BaseGraph graph : UnitController.getInstance().getGraphSet()) {
				if (graph instanceof TriangleGraph) {
					//识别线和三角形
				}
				if (graph instanceof CircleGraph) {
					//识别线和圆
				}
			}
			
			//找到所有与curLine一端相连的线的另一个端点
			Set<PointUnit> linkPointUnits1 = getLinkPointUnits(curLine.getEnd1());
			Set<PointUnit> linkPointUnits2 = getLinkPointUnits(curLine.getEnd2());
			
			for (BaseUnit unit : UnitController.getInstance().getUnits().values()) {
				if (unit instanceof LineUnit && unit != curLine) {
					//识别线和线
					LineUnit temp = (LineUnit) unit;
					
					if (pointToLine(curLine.getEnd1(), temp, linkPointUnits1)) {
						//如果有新的约束 则要更新相连的点链表
						linkPointUnits1 = getLinkPointUnits(curLine.getEnd1());
						linkPointUnits2 = getLinkPointUnits(curLine.getEnd2());
						continue;
					}
					if (pointToLine(curLine.getEnd2(), temp, linkPointUnits2)) {
						//如果有新的约束 则要更新相连的点链表
						linkPointUnits1 = getLinkPointUnits(curLine.getEnd1());
						linkPointUnits2 = getLinkPointUnits(curLine.getEnd2());
						continue;
					}
				}
			}
		}
	}
	
	/**
	 * 找到所有与curPointUnit相连的线的端点, 如curLine（a,b）,有线(c,d),(e,f)与之相连(ac重合,de重合)则返回a,c,d,e
	 * @param curPointUnit
	 * @return
	 */
	static private Set<PointUnit> getLinkPointUnits(PointUnit curPointUnit) {
		Set<PointUnit> linkPointUnits = new HashSet<PointUnit>();
		
		Set<PointUnit> tempSet = new HashSet<PointUnit>();
		tempSet.add(curPointUnit);
		if (CstPointsSamePos.cstMap.get(curPointUnit) != null) {
			tempSet.addAll(CstPointsSamePos.cstMap.get(curPointUnit));
		}
		
		linkPointUnits.addAll(tempSet);
		
		addPoinUnit(linkPointUnits, tempSet, 1);
		
		return linkPointUnits;
	}
	
	static private void addPoinUnit(Set<PointUnit> linkPointUnits, Set<PointUnit> tempSet, int level) {
		for (BaseUnit unit : UnitController.getInstance().getUnits().values()) {
			if (unit instanceof LineUnit) {
				LineUnit temp = (LineUnit) unit;
				//如果getEnd1与curLine的端点约束
				if (tempSet.contains(temp.getEnd1())) {
					linkPointUnits.add(temp.getEnd2());
					if (CstPointsSamePos.cstMap.get(temp.getEnd2()) != null) {
						linkPointUnits.addAll(CstPointsSamePos.cstMap.get(temp.getEnd2()));
						if (level < 2) {
							addPoinUnit(linkPointUnits, CstPointsSamePos.cstMap.get(temp.getEnd2()), level+1);
						}
					}
				}
				//如果getEnd2与curLine的端点约束
				if (tempSet.contains(temp.getEnd2())) {
					linkPointUnits.add(temp.getEnd1());
					if (CstPointsSamePos.cstMap.get(temp.getEnd1()) != null) {
						linkPointUnits.addAll(CstPointsSamePos.cstMap.get(temp.getEnd1()));
						if (level < 2) {
							addPoinUnit(linkPointUnits, CstPointsSamePos.cstMap.get(temp.getEnd1()), level + 1);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 识别pointUnit与lineUnit端点是否约束，并且lineUnit端点不在pSet内
	 * @param pointUnit
	 * @param lineUnit
	 * @param pSet
	 * @return
	 */
	static private boolean pointToLine(PointUnit pointUnit, LineUnit lineUnit, Set<PointUnit> linkPointUnits) {
		if (CommonFunction.distance(pointUnit.toPoint(), lineUnit.getEnd1()
				.toPoint()) < ThresholdProperty.TWO_POINT_IS_CONSTRAINTED
				&& (linkPointUnits == null || !linkPointUnits.contains(lineUnit.getEnd1()))) {
			pointUnit.Set(lineUnit.getEnd1());
			CstPointsSamePos.Add(pointUnit, lineUnit.getEnd1());
			return true;
		}
		if (CommonFunction.distance(pointUnit.toPoint(), lineUnit.getEnd2()
				.toPoint()) < ThresholdProperty.TWO_POINT_IS_CONSTRAINTED
				&& (linkPointUnits == null || !linkPointUnits.contains(lineUnit.getEnd2()))) {
			pointUnit.Set(lineUnit.getEnd2());
			CstPointsSamePos.Add(pointUnit, lineUnit.getEnd2());
			return true;
		}
		return false;
	}
	
	/**
	 * 每次添加一个约束，合并一次groupID，保证有约束的图元、图形group一致
	 * @param group1
	 * @param group2
	 */
	public static void union(long group1, long group2) {
		if (group1 == group2) return;
		//把所有group为group1的设置为group2
		for (BaseGraph graph : UnitController.getInstance().getGraphSet()) {
			if (graph.getGroup() == group1) {
				graph.setGroup(group2);
			}
		}
		for (BaseUnit unit : UnitController.getInstance().getUnitSet()) {
			if (unit.getGroup() == group1) {
				unit.setGroup(group2);
			}
		}
	}
	
	/**
	 * 获取与object相关的约束
	 * @param object
	 * @return
	 */
	static public List<SGObject> getRelateObjects(SGObject object) {
		List<SGObject> resList = new ArrayList<SGObject>();
		for (BaseGraph itg : UnitController.getInstance().getGraphSet()) {
			if (isRelate(itg, object))
				resList.add(itg);
		}
		for (BaseUnit itu : UnitController.getInstance().getUnitSet()) {
			if (isRelate(itu, object))
				resList.add(itu);
		}
		if (resList.size() != 0) return resList;
		return null;
	}
	
	static public boolean isRelate(SGObject o1, SGObject o2) {
		return CstCircleInTriangle.isRelated(o1, o2)
				|| CstPointsSamePos.isRelated(o1, o2)
				|| CstPointOnCircle.isRelated(o1, o2)
				|| CstTriangleInCircle.isRelated(o1, o2);
	}
	
}

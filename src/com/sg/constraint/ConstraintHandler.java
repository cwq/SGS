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
	static public void constraintRecognize(BaseUnit u) {
		if (u instanceof PointUnit) {
			PointUnit pointUnit = (PointUnit) u;
			for (BaseUnit unit : UnitController.getInstance().getUnits().values()) {
				if (unit instanceof LineUnit && !unit.contains(pointUnit)) {
					//识别线和线
					LineUnit temp = (LineUnit) unit;
					pointToLine(pointUnit, temp);
				}
			}
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
			for (BaseUnit unit : UnitController.getInstance().getUnits().values()) {
				if (unit instanceof LineUnit && unit != curLine) {
					//识别线和线
					LineUnit temp = (LineUnit) unit;
					//如果已经有一点约束则不识别 避免重叠成一条线
					if (CstPointsSamePos.isRelated(temp, curLine)) continue;
					
					if (pointToLine(curLine.getEnd1(), temp)) continue;
					if (pointToLine(curLine.getEnd2(), temp)) continue;
				}
			}
		}
	}
	
	/**
	 * 识别pointUnit与lineUnit端点是否重合约束
	 * @param pointUnit
	 * @param lineUnit
	 * @return
	 */
	static private boolean pointToLine(PointUnit pointUnit, LineUnit lineUnit) {
		if (CommonFunction.distance(pointUnit.toPoint(),
				lineUnit.getEnd1().toPoint()) < ThresholdProperty.TWO_POINT_IS_CONSTRAINTED) {
			pointUnit.Set(lineUnit.getEnd1());
			CstPointsSamePos.Add(pointUnit, lineUnit.getEnd1());
			return true;
		}
		if (CommonFunction.distance(pointUnit.toPoint(),
				lineUnit.getEnd2().toPoint()) < ThresholdProperty.TWO_POINT_IS_CONSTRAINTED) {
			pointUnit.Set(lineUnit.getEnd2());
			CstPointsSamePos.Add(pointUnit, lineUnit.getEnd2());
			return true;
		}
		return false;
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

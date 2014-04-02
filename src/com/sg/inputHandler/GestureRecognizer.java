package com.sg.inputHandler;

import java.util.List;
import com.sg.controller.UnitController;
import com.sg.property.common.CommonFunction;
import com.sg.property.common.Point;
import com.sg.property.common.ThresholdProperty;
import com.sg.unit.BaseUnit;
import com.sg.unit.LineUnit;

/**
 * 手势识别
 * @author DELL
 *
 */
public class GestureRecognizer {
	
	private static GestureRecognizer instance = new GestureRecognizer();
	
	public static GestureRecognizer getInstance() {
		return instance;
	}
	
	private GestureRecognizer() {
		
	}
	
	/**
	 * 单点手势识别
	 * @param p1
	 */
	public void recognize(List<Point> pList) {
		int size = pList.size();
		BaseUnit select = UnitController.getInstance().getSelectUnit();
		if (select instanceof LineUnit) {
			LineUnit line = (LineUnit) select;
			if (line.getEnd1().isInUnit(pList.get(0))) {
				line.getEnd1().Set(pList.get(size - 1));
			}
			else if (line.getEnd2().isInUnit(pList.get(0))) {
				line.getEnd2().Set(pList.get(size - 1));
			}
			else {
				line.translate(new Point(pList.get(size - 1).getX() - pList.get(size - 2).getX(), 
						pList.get(size - 1).getY() - pList.get(size - 2).getY()));
			}
		}
	}
	
	public void recognize(Point p1, Point p2) {
		
	}
	
	public void setFirstPoint(Point fp) {
	
	}
	
	public boolean isSelectUnit(List<Point> pList) {
		int n = pList.size();
		Point cPoint = pList.get(n/2);
		
		int count = 0;     //在圆外的点数量
		for(Point point : pList) {
			if(CommonFunction.distance(cPoint, point) > ThresholdProperty.POINT_DISTANCE) {
				count++;
			}
		}
		
		if (5*count > n) {
			return false;
		} else {
			return UnitController.getInstance().selectUnit(cPoint);
		}
	}

}

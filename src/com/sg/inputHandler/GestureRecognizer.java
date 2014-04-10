package com.sg.inputHandler;

import java.util.List;
import com.sg.controller.UnitController;
import com.sg.object.Point;
import com.sg.property.common.CommonFunction;
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
	/**
	 * 0:还未识别是否点中点元
	 * 1：代表选中end1
	 * 2：代表选中end2
	 * 3：代表识别完 没有选中点
	 */
	private int whichPoint = 0;
	
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
			if (whichPoint == 0) {
				//第一次识别是否选中点
				if (line.getEnd1().isInObject(pList.get(0))) {
					whichPoint = 1;
				}
				else if (line.getEnd2().isInObject(pList.get(0))) {
					whichPoint = 2;
				} else {
					whichPoint = 3;
				}
			}
			if (whichPoint == 1) {
				//选择第一个点
				line.getEnd1().Set(pList.get(size - 1));
			}
			if (whichPoint == 2) {
				//选择第一个点
				line.getEnd2().Set(pList.get(size - 1));
			}
			if (whichPoint == 3) {
				//移动线
				line.translate(new Point(pList.get(size - 1).getX() - pList.get(size - 2).getX(), 
						pList.get(size - 1).getY() - pList.get(size - 2).getY()));
			}
		} else {
			select.translate(new Point(pList.get(size - 1).getX() - pList.get(size - 2).getX(), 
					pList.get(size - 1).getY() - pList.get(size - 2).getY()));
		}
	}
	
	public void recognize(Point p1, Point p2) {
		
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

	public void setWhichPoint(int whichPoint) {
		this.whichPoint = whichPoint;
	}

}

package com.sg.inputHandler;

import java.util.List;
import com.sg.controller.UnitController;
import com.sg.property.common.CommonFunction;
import com.sg.property.common.Point;
import com.sg.property.common.ThresholdProperty;

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
	
	public void recognize(Point p1) {
		//handle
		//if 不是手势  Preprocessing.preprocess
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

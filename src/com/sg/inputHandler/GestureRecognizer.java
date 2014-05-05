package com.sg.inputHandler;

import java.util.Collection;
import java.util.List;

import com.sg.constraint.UnitChangeArgs;
import com.sg.controller.UnitController;
import com.sg.object.Point;
import com.sg.property.common.CommonFunction;
import com.sg.property.common.ThresholdProperty;
import com.sg.unit.BaseUnit;
import com.sg.unit.LineUnit;
import com.sg.unit.PointUnit;

/**
 * 手势识别
 * @author DELL
 *
 */
public class GestureRecognizer {
	
	private static GestureRecognizer instance = new GestureRecognizer();
//	/**
//	 * 0:还未识别是否点中点元
//	 * 1：代表选中end1
//	 * 2：代表选中end2
//	 * 3：代表识别完 没有选中点
//	 */
//	private int whichPoint = 0;
	
	//为拖拽的点
	private PointUnit curPointUnit = null;
	
	//只有第一次判断是否拖拽点
	private boolean isFirst = true;
	
	/**
	 * 是否为双点手势，为处理双点手势弹起，双点手势move过程不能直接进入单点手势
	 */
	private boolean isDouble = false;
	
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
		if (isDouble) {
			return;
		}
		int size = pList.size();
		Collection<BaseUnit> selects = UnitController.getInstance().getSelects();
		if (selects.size() == 0) {
			return;
		}

		if (isFirst) {
			isFirst = false;
			//判断是否拖拽点
			for (BaseUnit baseUnit : selects) {
				if (baseUnit instanceof LineUnit) {
					if (((LineUnit) baseUnit).getEnd1().isInObject(pList.get(0))) {
						curPointUnit = ((LineUnit) baseUnit).getEnd1();
						return;
					}
					if (((LineUnit) baseUnit).getEnd2().isInObject(pList.get(0))) {
						curPointUnit = ((LineUnit) baseUnit).getEnd2();
						return;
					}
				}
			}
		} else {
			if (curPointUnit != null) {
				//拖拽点
				curPointUnit.Set(pList.get(size-1));
			} else {
				
				if (selects.size() == 1) {
					//平移单个
					for (BaseUnit baseUnit : selects) {
						baseUnit.translate(
								new Point(pList.get(size - 1).getX()
										- pList.get(size - 2).getX(), pList
										.get(size - 1).getY()
										- pList.get(size - 2).getY()), null);
					}
				} else {
					//平移所有
					UnitChangeArgs e = null;
					boolean first = true;
					for (BaseUnit baseUnit : selects) {
						if (first) {
							e = new UnitChangeArgs(baseUnit, 0, 0);
							isFirst = false;
						}
						baseUnit.translate(
								new Point(pList.get(size - 1).getX()
										- pList.get(size - 2).getX(), pList
										.get(size - 1).getY()
										- pList.get(size - 2).getY()), e);
					}
				}
			}
		}
	}
	
	public void recognize(Point p1, Point p2) {
		isDouble = true;
	}
	
	/**
	 * 是否是选中手势，并且选中单个图元
	 * @param pList
	 * @return
	 */
	public boolean isSelectOneUnit(List<Point> pList) {
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
			return UnitController.getInstance().selectOneUnit(cPoint);
		}
	}
	
	/**
	 * 判断点是否点在选中的图元上
	 * @param point
	 * @return
	 */
	public boolean isInSelects(Point point) {
		for (BaseUnit unit : UnitController.getInstance().getSelects()) {
			if (unit.isInObject(point)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 在up后执行  清楚一些数据记录
	 */
	public void init() {
//		whichPoint = 0;
		isDouble = false;
		curPointUnit = null;
		isFirst = true;
	}

}

package com.sg.inputHandler;

import java.util.List;

import android.util.Log;

import com.sg.constraint.ConstraintHandler;
import com.sg.constraint.CstPointsSamePos;
import com.sg.controller.UnitController;
import com.sg.object.Point;
import com.sg.property.common.CommonFunction;
import com.sg.property.common.ThresholdProperty;
import com.sg.unit.BaseUnit;
import com.sg.unit.CurveUnit;
import com.sg.unit.LineUnit;
import com.sg.unit.PointUnit;
import com.sg.unit.SketchUnit;

/**
 * 图元识别
 * @author DELL
 *
 */
public class UnitRecognizer {
	
	private static UnitRecognizer instance = new UnitRecognizer();
	
	private BaseUnit lastUnit;
	private boolean isAdjust = false;
	private Point startAdjustPoint;
	private Point endAdjustPoint;
	private double minDis;

	private UnitRecognizer() {
		
	}
	
	public static UnitRecognizer getInstance() {
		return instance;
	}
	
	
	/**
	 * 对时间阈值内收集的点进行处理  识别
	 * @param points
	 */
	public void recognizeFirstPart(List<Point> points) {
		SpecialPointRecognizer.getInstance().gaussProcessing(points);
		List<Integer> ins = SpecialPointRecognizer.getInstance().getSpecialPointIndex(points);
		if (ins.size() < 2)  return;
		if (ins.size() == 2) {
			if (((LineUnit) (new LineUnit())).Adapt(points)) {
				//识别直线
				lastUnit = new LineUnit(points.get(ins.get(0)), points.get(ins.get(1)));
				UnitController.getInstance().addUnit(lastUnit);
				
//				//识别第一个点的约束
//				ConstraintHandler.constraintRecognize(((LineUnit) lastUnit).getEnd1());
			} else {
				//识别曲线
				lastUnit = new CurveUnit();
				if(((CurveUnit)lastUnit).Adapt(points) == true) {
					UnitController.getInstance().addUnit(lastUnit);
				} else {
					//草图
					lastUnit =  null;
					return;
				}
			}
		}
		if (ins.size() > 2) {
			//折线
			for (int i = 0; i < ins.size() - 1; i++) {
				LineUnit temp = new LineUnit(points.get(ins.get(i)), points.get(ins.get(i+1)));
//				if (i == 0) {
//					//识别第一个点的约束
//					ConstraintHandler.constraintRecognize(temp.getEnd1());
//				}
//				if (i > 0) {
//					//添加约束
//					CstPointsSamePos.Add(((LineUnit) lastUnit).getEnd2(), temp.getEnd1());
//				}
				lastUnit = temp;
				UnitController.getInstance().addUnit(lastUnit);
			}
		} 
		
	}
	
	//onMove  还在移动中
	public void recognizeUnitOnMove(Point p) {
		if (lastUnit == null)  return;
		if (lastUnit instanceof LineUnit) {
			LineUnit temp = (LineUnit) lastUnit;
			double A = CommonFunction.distance(temp.getEnd1().toPoint(), p);
			double B = CommonFunction.distance(temp.getEnd2().toPoint(), p);
			double C = CommonFunction.distance(temp.getEnd1().toPoint(), ((LineUnit) lastUnit).getEnd2().toPoint());
			double disL = CommonFunction.distance(temp.getEnd1().toPoint(), temp.getEnd2().toPoint());
			
			double tmp = B * B + C * C - A * A;
			tmp = tmp / (2 * B * C + 0.00001);
			double ridian = Math.acos(tmp);
			
			if (isAdjust) {
				((LineUnit) lastUnit).setEnd2(new PointUnit(p));
				if (B < minDis) {
					minDis = B;
					endAdjustPoint = p;
				}
				if (CommonFunction.distance(startAdjustPoint, p) > ThresholdProperty.GRAPH_CHECKED_DISTANCE) {
					//退出微调
					Log.v("Adjust", "退出微调");
					isAdjust = false;
					((LineUnit) lastUnit).setEnd2(new PointUnit(endAdjustPoint));
				}
				return;
			}
			
			if (!isAdjust && B < ThresholdProperty.POINT_DISTANCE) {
				//进入微调
				Log.v("Adjust", "进入微调");
				isAdjust = true;
				startAdjustPoint = p;
				endAdjustPoint = p;
				minDis = B;
				((LineUnit) lastUnit).setEnd2(new PointUnit(p));
				return;
			}
			
			if (ridian * 180.0 / Math.PI <= 150.0 && disL > ThresholdProperty.TWO_POINT_IS_CLOSED) {
				
				lastUnit = new LineUnit(temp.getEnd2().toPoint(), p);
				UnitController.getInstance().addUnit(lastUnit);
//				//添加约束
//				CstPointsSamePos.Add(temp.getEnd2(), ((LineUnit) lastUnit).getEnd1());
				
			} else {
				((LineUnit) lastUnit).setEnd2(new PointUnit(p));
			}
		}
		if (lastUnit instanceof CurveUnit) {
			CurveUnit temp = (CurveUnit) lastUnit;
			if (temp.Adapt(UnitController.getInstance().getSketchUnit().getPoints()) == false) {
				UnitController.getInstance().deleteUnit(lastUnit);
				lastUnit = null;
			}
		}
	}
	
	public BaseUnit recognizeUnitOnUp(List<Point> points, int state) {
		isAdjust = false;
		UnitController.getInstance().setSelectUnit(null);
		int n = points.size();
		double totalLength = 0;
		for(int i = 0; i < n-1; i++) {
			totalLength += CommonFunction.distance(points.get(i), points.get(i+1));
		}
		//画的距离太短 忽略
		if (totalLength < ThresholdProperty.TWO_POINT_IS_CONSTRAINTED) {
			return null;
		}
		//时间很短
		if (state == 0) {
			recognizeFirstPart(points);
		}
		//草图
		if (lastUnit == null) {
			SketchUnit sketchUnit =  new SketchUnit();
			for (int i = 0; i < n; i++) {
				sketchUnit.addPoint(points.get(i));
			}
			UnitController.getInstance().addUnit(sketchUnit);
			lastUnit = sketchUnit;
		}

		return lastUnit;
	}
	
}

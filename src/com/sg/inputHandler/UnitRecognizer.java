package com.sg.inputHandler;

import java.util.ArrayList;
import java.util.List;

import android.net.NetworkInfo.State;
import android.util.Log;

import com.sg.controller.UnitController;
import com.sg.property.common.CommonFunction;
import com.sg.property.common.Point;
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
	
	
	private boolean isLine(List<Point> points) {
		int n = points.size();
		if (n < 2) {
			return false;
		}
        //判断是否是直线图元的方法：若首末两点的距离比上所有的两两相邻的点之间的距离之和，比之大于阀值的话，则判断为直线图元
        //阀值暂定为0.95
		double totalLength = 0.0;
		double tmpLength = CommonFunction.distance(points.get(0), points.get(n-1));
		
		if (tmpLength < ThresholdProperty.TWO_POINT_IS_CONSTRAINTED) {
			return false;
		}
		for(int i = 0; i < n-1; i++) {
			totalLength += CommonFunction.distance(points.get(i), points.get(i+1));
		}
		
		if(tmpLength / totalLength >= 0.98) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 对时间阈值内收集的点进行处理  识别
	 * @param points
	 */
	public void recognizeFirstPart(List<Point> points) {
//		SpecialPointRecognizer.getInstance().gaussProcessing(points);
		List<Integer> ins = SpecialPointRecognizer.getInstance().getSpecialPointIndex(points);
		if (ins.size() > 2) {
			//折线
			for (int i = 0; i < ins.size() - 1; i++) {
				lastUnit = new LineUnit(points.get(ins.get(i)), points.get(ins.get(i+1)));
				UnitController.getInstance().addUnit(lastUnit);
			}
		} else {
			if (isLine(points) && ins.size() == 2) {
				lastUnit = new LineUnit(points.get(ins.get(0)), points.get(ins.get(1)));
				UnitController.getInstance().addUnit(lastUnit);
			} else {
				lastUnit = new CurveUnit();
			}
		}
		
	}
	
	//onMove  还在移动中
	public void recognizeUnitOnMove(Point p) {
		if (lastUnit instanceof LineUnit) {
			LineUnit temp = (LineUnit) lastUnit;
			double A = CommonFunction.distance(temp.getEnd1().getPoint(), p);
			double B = CommonFunction.distance(temp.getEnd2().getPoint(), p);
			double C = CommonFunction.distance(temp.getEnd1().getPoint(), ((LineUnit) lastUnit).getEnd2().getPoint());
			double disL = CommonFunction.distance(temp.getEnd1().getPoint(), temp.getEnd2().getPoint());
			
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
				
				lastUnit = new LineUnit(temp.getEnd2().getPoint(), p);
				UnitController.getInstance().addUnit(lastUnit);
			} else {
				((LineUnit) lastUnit).setEnd2(new PointUnit(p));
			}
		} else {

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
		if (totalLength < ThresholdProperty.TWO_POINT_IS_CONSTRAINTED) {
			return null;
		}
		if (state == 0) {
			recognizeFirstPart(points);
		}
		if (lastUnit instanceof CurveUnit) {
			
			SketchUnit sketchUnit =  new SketchUnit();
			for (int i = 0; i < n; i++) {
				sketchUnit.addPoint(points.get(i));
			}
			UnitController.getInstance().addUnit(sketchUnit);
			lastUnit = sketchUnit;
		}
//		SpecialPointRecognizer specialPointRecognizer = SpecialPointRecognizer.getInstance();
//		//specialPointRecognizer.gaussProcessing(points);
//		List<Integer> ins = specialPointRecognizer.getSpecialPointIndex(points);
//		SketchUnit sketchUnit =  new SketchUnit();
//		for (int i = 0; i < ins.size(); i++) {
//			sketchUnit.addPoint(points.get(ins.get(i)));
//		}
//		UnitController.getInstance().addUnit(sketchUnit);
//		return sketchUnit;
		return lastUnit;
	}
	
}

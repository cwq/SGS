package com.sg.inputHandler;

import java.util.ArrayList;
import java.util.List;

import com.sg.controller.UnitController;
import com.sg.property.common.Point;
import com.sg.unit.BaseUnit;
import com.sg.unit.SketchUnit;

/**
 * 图元识别
 * @author DELL
 *
 */
public class UnitRecognizer {
	
	private static UnitRecognizer instance = new UnitRecognizer();

	private UnitRecognizer() {
		
	}
	
	public static UnitRecognizer getInstance() {
		return instance;
	}
	
	//onMove  还在移动中
	public List<Point> recognizeUnitOnMove(List<Point> points) {
		List<Point> res = new ArrayList<Point>();
		res.add(points.get(0));
		res.add(points.get(points.size()-1));
		return res;
	}
	
	public BaseUnit recognizeUnitOnUp(List<Point> points) {
		UnitController.getInstance().setSelectUnit(null);
		
		SketchUnit sketchUnit =  new SketchUnit();
		for (int i = 0; i < points.size(); i++) {
			sketchUnit.addPoint(points.get(i));
		}
		UnitController.getInstance().addUnit(sketchUnit);
		return sketchUnit;
	}
	
}

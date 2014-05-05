package com.sg.controller;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Canvas;
import android.graphics.Color;
import com.sg.constraint.ConstraintHandler;
import com.sg.graph.BaseGraph;
import com.sg.graph.CircleGraph;
import com.sg.object.Point;
import com.sg.property.common.ThresholdProperty;
import com.sg.property.tools.Painter;
import com.sg.unit.BaseUnit;
import com.sg.unit.CurveUnit;
import com.sg.unit.LineUnit;
import com.sg.unit.SketchUnit;

public class UnitController {
	
	private static UnitController instance = new UnitController();
	
	private ConcurrentHashMap<Long, BaseUnit> units;
	
	private ConcurrentHashMap<String, BaseGraph> graphs;
	
//	//选择的单个图元
//	private BaseUnit selectUnit;
	
	private ConcurrentHashMap<Long, BaseUnit> selects;
	
	//画图过程的草图
	private SketchUnit drawingSketch;
	//变画边识别的图元 还未添加到units
	private BaseUnit preViewUnit;
	
	private Painter painter;
	private Painter checkedPainter;
	
	private UnitController() {
		units = new ConcurrentHashMap<Long, BaseUnit>();
		graphs = new ConcurrentHashMap<String, BaseGraph>();
		selects = new ConcurrentHashMap<Long, BaseUnit>();
		preViewUnit = null;
		drawingSketch = new SketchUnit();
		units.put(drawingSketch.getID(), drawingSketch);
		painter = new Painter(Color.BLACK, ThresholdProperty.DRAW_WIDTH);
		checkedPainter = new Painter(Color.RED, ThresholdProperty.DRAW_WIDTH);
	}
	
	public static UnitController getInstance() {
		return instance;
	}
	
	public void addUnit(BaseUnit u) {
		units.put(u.getID(), u);
		if (u instanceof LineUnit) {
			ConstraintHandler.constraintRecognize(u);
		}
		if (u instanceof CurveUnit) {
			if (((CurveUnit) u).isCircle()) {
				CircleGraph circleGraph = new CircleGraph((CurveUnit) u);
				addGraph(circleGraph);
				ConstraintHandler.constraintRecognize(circleGraph);
			}
		}
	}
	
	public void addGraph(BaseGraph g) {
		if (!graphs.containsKey(g.getKey())) {
			graphs.put(g.getKey(), g);
		}
	}
	
	public void deleteUnit(BaseUnit u) {
		units.remove(u.getID());
	}
	
	public void deleteGraph(BaseGraph g) {
		graphs.remove(g);
	}
	
	public void clear() {
		units.clear();
		drawingSketch.clear();
		units.put(drawingSketch.getID(), drawingSketch);
//		selectUnit = null;
		selects.clear();
		preViewUnit = null;
		graphs.clear();
	}
	
	public Collection<BaseUnit> getSelects() {
		return selects.values();
	}
	
	public void addSelect(BaseUnit unit) {
		selects.put(unit.getID(), unit);
	}
	
	public void setNoSelect() {
		selects.clear();
	}
	
//	public BaseUnit getSelectUnit() {
//		return selectUnit;
//	}
//
//	public void setSelectUnit(BaseUnit selectUnit) {
//		this.selectUnit = selectUnit;
//	}
	
	public SketchUnit getSketchUnit() {
		return drawingSketch;
	}
	
	public synchronized void setPreViewUnit(BaseUnit preViewUnit) {
		this.preViewUnit = preViewUnit;
	}
	
	public BaseUnit getPreViewUnit() {
		return preViewUnit;
	}
	
	public ConcurrentHashMap<Long, BaseUnit> getUnits() {
		return units;
	}
	
	public Collection<BaseUnit> getUnitSet() {
		return units.values();
	}
	
	public Collection<BaseGraph> getGraphSet() {
		return graphs.values();
	}
	
	/**
	 * 获取p点中的图元
	 * @param p
	 * @return
	 */
	private BaseUnit selectUnit(Point p) {
		Iterator<Long> iter = units.keySet().iterator();
		BaseUnit u;
		Long key;
		while (iter.hasNext()) {
			key = iter.next();
			if(units.containsKey(key)) {
				u = units.get(key);
				if (u != null && u != drawingSketch) {
					if(u.isInObject(p)) {
						return u;
					}
				}
			}
		}
		return null;
	}
	
	public boolean selectOneUnit(Point p) {
		setNoSelect();
		BaseUnit baseUnit = selectUnit(p);
		if (baseUnit != null) {
			addSelect(baseUnit);
			return true;
		}
		return false;
	}
	
	public boolean selectGroup(Point p) {
		setNoSelect();
		BaseUnit baseUnit = selectUnit(p);
		if (baseUnit != null) {
			for (BaseUnit unit : getUnitSet()) {
				if (unit.getGroup() == baseUnit.getGroup()) {
					addSelect(unit);
				}
			}
			return true;
		}
		return false;
	}
	
	public void draw(Canvas canvas) {
//		Iterator<Long> iter = units.keySet().iterator();
//		BaseUnit u;
//		Long key;
//		while (iter.hasNext()) {
//			key = iter.next();
//			if(units.containsKey(key)) {
//				u = units.get(key);
//				if (u != null) {
//					if(u == selectUnit)
//						u.draw(canvas, checkedPainter);
//					else
//						u.draw(canvas, painter);
//				}
//			}
//		}
		//drawingSketch.draw(canvas, painter);
		synchronized (this) {
			if (preViewUnit != null) {
				preViewUnit.draw(canvas, painter);
			}
		}
		
		for (BaseUnit u : units.values()) {
			if (u != null) {
				if (selects.containsKey(u.getID()))
					u.draw(canvas, checkedPainter);
				else
					u.draw(canvas, painter);
			}
			
		}
	}
}

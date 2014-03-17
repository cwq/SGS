package com.sg.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Canvas;
import android.graphics.Color;

import com.sg.constraint.ConstraintHandler;
import com.sg.graph.BaseGraph;
import com.sg.property.common.Point;
import com.sg.property.common.ThresholdProperty;
import com.sg.property.tools.Painter;
import com.sg.unit.BaseUnit;
import com.sg.unit.SketchUnit;

public class UnitController {
	
	private static UnitController instance = new UnitController();
	
	private ConcurrentHashMap<Long, BaseUnit> units;
	
	private List<BaseGraph> graphs;
	
	private BaseUnit selectUnit;
	
	private SketchUnit drawingSketch;
	
	private Painter painter;
	private Painter checkedPainter;
	
	private UnitController() {
		units = new ConcurrentHashMap<Long, BaseUnit>();
		graphs = new ArrayList<BaseGraph>();
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
	}
	
	public void addGraph(BaseGraph g) {
		graphs.add(g);
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
		selectUnit = null;
		graphs.clear();
	}
	
	public BaseUnit getSelectUnit() {
		return selectUnit;
	}

	public void setSelectUnit(BaseUnit selectUnit) {
		this.selectUnit = selectUnit;
	}
	
	public SketchUnit getSketchUnit() {
		return drawingSketch;
	}
	
	public ConcurrentHashMap<Long, BaseUnit> getUnits() {
		return units;
	}
	
	public boolean selectUnit(Point p) {
		Iterator<Long> iter = units.keySet().iterator();
		BaseUnit u;
		Long key;
		while (iter.hasNext()) {
			key = iter.next();
			if(units.containsKey(key)) {
				u = units.get(key);
				if (u != null && u != drawingSketch) {
					if(u.isInUnit(p)) {
						setSelectUnit(u);
						return true;
					}
				}
			}
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
		ConstraintHandler.Update();
		for (BaseUnit u : units.values()) {
			if(u == selectUnit)
				u.draw(canvas, checkedPainter);
			else
				u.draw(canvas, painter);
		}
	}
}

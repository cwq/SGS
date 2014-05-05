package com.sg.graph;

import android.graphics.Canvas;

import com.sg.constraint.UnitChangeArgs;
import com.sg.object.Point;
import com.sg.object.SGObject;
import com.sg.property.tools.Painter;

public class BaseGraph extends SGObject {
	
	//标识图形 避免重复添加
	private String key;

	@Override
	public void draw(Canvas canvas, Painter painter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInObject(Point point) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void translate(Point vector, UnitChangeArgs e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scale(Point vector, UnitChangeArgs e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotate(double rotateAngle, UnitChangeArgs e) {
		// TODO Auto-generated method stub
		
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}

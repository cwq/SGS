package com.sg.unit;

import java.util.List;

import android.graphics.Canvas;

import com.sg.constraint.UnitChangeArgs;
import com.sg.object.Point;
import com.sg.property.tools.Painter;

public class SpecialUnit extends BaseUnit {

	private List<BaseUnit> units;

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
}

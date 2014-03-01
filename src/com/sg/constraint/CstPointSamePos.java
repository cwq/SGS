package com.sg.constraint;

import com.sg.unit.*;

public class CstPointSamePos extends BaseConstraint {

	public PointUnit a;
	public PointUnit b;
	
	public CstPointSamePos(PointUnit a, PointUnit b) {
		this.a = a;
		this.b = b;
		ConstraintHandler.AddChanged(a);
		ConstraintHandler.AddChanged(b);
	}
	
	@Override
	public void UpdateConstraint() {
		if (ConstraintHandler.IsChanged(a))
			b.Set(a);
		else if (ConstraintHandler.IsChanged(b))
			a.Set(b);
	}

	@Override
	public boolean isInConstraint(BaseUnit unit) {
		// TODO Auto-generated method stub
		return false;
	}
}

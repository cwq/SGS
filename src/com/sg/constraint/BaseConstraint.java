package com.sg.constraint;

import com.sg.unit.BaseUnit;

public abstract class BaseConstraint {
	
	public abstract void UpdateConstraint();
	public abstract boolean isInConstraint(BaseUnit unit);
	
}
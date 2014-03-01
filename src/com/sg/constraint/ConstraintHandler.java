package com.sg.constraint;
import java.util.*;
import com.sg.unit.*;


public class ConstraintHandler {
	
	static private long changeKey = Long.MIN_VALUE;
	static private Map<Long, Long> ucKeys = new HashMap<Long, Long>();
	static private List<BaseConstraint> cstList = new ArrayList<BaseConstraint>();
	
	static public void Update()	{
		for(int i=cstList.size()-1; i>=0; i--) {
			cstList.get(i).UpdateConstraint();
		}
		IncChangedKey();
	}

	/**
	 * 识别约束
	 * @param u
	 */
	static public void constraintRecognize(BaseUnit u) {
		
	}
	
	/**
	 * 获取与u相关的约束
	 * @param u
	 * @return
	 */
	static List<BaseConstraint> getConstraints(BaseUnit u) {
		return null;
	}
	
	static public boolean IsInUcKeys(BaseUnit u) {
		return ucKeys.containsKey(u.getID());
	}
	
	static public long GetChangedKey() {
		return changeKey;
	}
	static private void IncChangedKey() {
		changeKey++;
	}
	
	static public void AddChanged(BaseUnit u) {
		ucKeys.put(u.getID(), GetChangedKey());
	}
	
	static public boolean IsChanged(BaseUnit u) {
		return ucKeys.get(u.getID()) == GetChangedKey();
	}
	
	static public void Changed(BaseUnit u) {
		ucKeys.put(u.getID(), GetChangedKey());
	}
	
	static public long getChangeKey() {
		return changeKey;
	}
	static public void setChangeKey(long changeKey) {
		ConstraintHandler.changeKey = changeKey;
	}
}

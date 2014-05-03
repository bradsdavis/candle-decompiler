package org.candle.decompiler.intermediate.code;

import java.util.Comparator;

import org.candle.decompiler.intermediate.expression.Case;

public class CaseComparator implements Comparator<Case> {

	@Override
	public int compare(Case c1, Case c2) {
		if(c1.getInstructionHandle().getPosition() < c2.getInstructionHandle().getPosition()) {
			return -1;
		}
		else if(c1.getInstructionHandle().getPosition() > c2.getInstructionHandle().getPosition()) {
			return 1;
		}
		
		return 0;
	}


}

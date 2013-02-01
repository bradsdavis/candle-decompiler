package org.candle.decompiler.intermediate.code;

import java.util.Comparator;

public class IntermediateComparator implements Comparator<AbstractIntermediate> {

	public boolean after(AbstractIntermediate o1, AbstractIntermediate o2) {
		return compare(o1, o2) > 0;
	}
	
	public boolean before(AbstractIntermediate o1, AbstractIntermediate o2) {
		return compare(o1, o2) < 0;
	}
	
	@Override
	public int compare(AbstractIntermediate o1, AbstractIntermediate o2) {
		return o1.getInstruction().getPosition() - o2.getInstruction().getPosition();
	}

}

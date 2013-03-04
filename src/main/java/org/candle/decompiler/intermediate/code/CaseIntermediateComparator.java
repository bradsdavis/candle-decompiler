package org.candle.decompiler.intermediate.code;

import java.util.Comparator;

public class CaseIntermediateComparator implements Comparator<CaseIntermediate> {

	@Override
	public int compare(CaseIntermediate c1, CaseIntermediate c2) {
		int c1Pos = c1.getBlockRange().getStart().getPosition();
		int c2Pos = c2.getBlockRange().getStart().getPosition();
		
		return c1Pos - c2Pos;
	}

}

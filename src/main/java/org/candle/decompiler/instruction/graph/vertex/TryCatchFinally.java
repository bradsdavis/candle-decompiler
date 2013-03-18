package org.candle.decompiler.instruction.graph.vertex;

import java.util.Set;

import org.apache.commons.lang.math.Range;

public class TryCatchFinally {

	private Range tryRange;
	private Set<Range> catchRanges;
	private Range finallyRange;
	
	public void setTryRange(Range tryRange) {
		this.tryRange = tryRange;
	}
	
	public void setCatchRanges(Set<Range> catchRanges) {
		this.catchRanges = catchRanges;
	}
	
	public void setFinallyRange(Range finallyRange) {
		this.finallyRange = finallyRange;
	}
}

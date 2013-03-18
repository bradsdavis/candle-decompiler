package org.candle.decompiler.instruction;

import org.apache.commons.lang.math.Range;
import org.apache.bcel.generic.InstructionHandle;

public class InstructionRange extends Range {

	public InstructionRange() {
		
	}
	
	public InstructionRange(InstructionHandle start, InstructionHandle end) {
		this.start = start;
		this.end = end;
	}
	
	private InstructionHandle start;
	private InstructionHandle end;
	
	public InstructionHandle getStart() {
		return start;
	}
	
	public void setStart(InstructionHandle start) {
		this.start = start;
	}
	
	public InstructionHandle getEnd() {
		return end;
	}
	
	public void setEnd(InstructionHandle end) {
		this.end = end;
	}
	
	@Override
	public Number getMinimumNumber() {
		return start.getPosition();
	}

	@Override
	public Number getMaximumNumber() {
		return end.getPosition();
	}

	@Override
	public boolean containsNumber(Number test) {
		if(getMaximumNumber() != null && getMinimumNumber() != null) {
			return (test.intValue() >= getMinimumNumber().intValue() && test.intValue() <= getMaximumNumber().intValue());
		}
		
		return false;
	}
	
}

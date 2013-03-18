package org.candle.decompiler.intermediate.code;

import org.apache.commons.lang.math.Range;
import org.apache.bcel.generic.InstructionHandle;


public class BlockRange extends Range {

	public BlockRange() {
		
	}
	
	public BlockRange(InstructionHandle start, InstructionHandle end) {
		this.start = start;
		this.end = end;
	}
	
	private InstructionHandle start;
	private InstructionHandle end;
	
	public InstructionHandle getStart() {
		return start;
	}
	
	public InstructionHandle getEnd() {
		return end;
	}
	
	public void setStart(InstructionHandle start) {
		this.start = start;
	}
	
	public void setEnd(InstructionHandle end) {
		this.end = end;
	}
	
	public boolean isRangeDetermined() {
		if(getMaximumNumber() != null && getMinimumNumber() != null) {
			if(start.getPosition() <= end.getPosition()) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean containsNumber(Number test) {
		if(getMaximumNumber() != null && getMinimumNumber() != null) {
			return (test.intValue() >= getMinimumNumber().intValue() && test.intValue() <= getMaximumNumber().intValue());
		}
		
		return false;
	}

	@Override
	public Number getMaximumNumber() {
		if(end != null) {
			return end.getPosition();
		}
		return null;
	}

	@Override
	public Number getMinimumNumber() {
		if(start != null) {
			return start.getPosition();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return " | Range["+getMinimumNumber()+", "+getMaximumNumber()+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockRange other = (BlockRange) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}
	
	
	
	
}

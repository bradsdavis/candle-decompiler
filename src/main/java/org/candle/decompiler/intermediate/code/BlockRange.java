package org.candle.decompiler.intermediate.code;

import org.apache.commons.lang.math.Range;


public class BlockRange extends Range {

	private Integer start;
	private Integer end;
	
	public void setStart(Integer start) {
		this.start = start;
	}
	
	public void setEnd(Integer end) {
		this.end = end;
	}
	
	
	public boolean isRangeDetermined() {
		if(getMaximumNumber() != null && getMinimumNumber() != null) {
			return true;
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
		return end;
	}

	@Override
	public Number getMinimumNumber() {
		return start;
	}
	
	@Override
	public String toString() {
		return " | Range["+start+", "+end+"]";
	}
	
	
}

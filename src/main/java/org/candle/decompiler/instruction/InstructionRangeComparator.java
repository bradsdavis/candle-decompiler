package org.candle.decompiler.instruction;

import java.util.Comparator;

public class InstructionRangeComparator implements Comparator<InstructionRange> {

	@Override
	public int compare(InstructionRange br1, InstructionRange br2) {
		if(br1.getStart().getPosition() == br2.getStart().getPosition()) {
			
			//check end... 
			if(br1.getEnd().getPosition() < br2.getEnd().getPosition()) {
				return 1;
			}
			else if(br1.getEnd().getPosition() > br2.getEnd().getPosition())
			{
				return -1;
			}
			else {
				return 0;
			}
		}
		if(br1.getStart().getPosition() < br2.getStart().getPosition()) {
			return -1;
		}
		return 1;
	}

}

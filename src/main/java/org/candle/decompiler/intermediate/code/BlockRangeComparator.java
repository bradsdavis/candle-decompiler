package org.candle.decompiler.intermediate.code;

import java.util.Comparator;

public class BlockRangeComparator implements Comparator<BlockRange> {

	@Override
	public int compare(BlockRange br1, BlockRange br2) {
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

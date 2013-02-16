package org.candle.decompiler.intermediate.graph.range;

import org.candle.decompiler.intermediate.code.conditional.IfIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;

public class IfUpperRangeVisitor extends GraphIntermediateVisitor {

	public IfUpperRangeVisitor(IntermediateGraphContext igc) {
		super(igc);
	}
	
	@Override
	public void visitIfLine(IfIntermediate line) {
		//ok, simplification of the algorithm for IF statement upper...
		
		//find the range on the if block, which is the TRUE branch to the FALSE branch.
		//now, loop through all of the statements within range...
		//if there is a GOTO within the range... then store it.
		//find the highest possible GOTO that passes the FALSE branch instruction.
		
		
	}

}

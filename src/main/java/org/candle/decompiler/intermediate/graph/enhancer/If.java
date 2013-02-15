package org.candle.decompiler.intermediate.graph.enhancer;

import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.conditional.IfIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;

/**
 * This enhancer transforms BooleanBranchIntermediate lines into If lines.
 * 
 * @author bradsdavis
 *
 */
public class If extends GraphIntermediateVisitor {

	public If(IntermediateGraphContext igc) {
		super(igc, false);
	}

	@Override
	public void visitBooleanBranchIntermediate(BooleanBranchIntermediate line) {
		
		//transform to IF block.
		
		IfIntermediate ifIntermediate = new IfIntermediate(line.getInstruction(), line.getExpression());
		igc.getIntermediateGraph().addVertex(ifIntermediate);
		
		//now, replace the vertex.
		igc.redirectPredecessors(line, ifIntermediate);
		igc.redirectSuccessors(line, ifIntermediate);
		
		igc.getIntermediateGraph().removeVertex(line);
	}
	

}

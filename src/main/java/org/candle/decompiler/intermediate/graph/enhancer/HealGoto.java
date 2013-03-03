package org.candle.decompiler.intermediate.graph.enhancer;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;

/***
 * Removes Goto Statements and redirects incoming to the outgoing.
 * 
 * A -> Goto -> B
 * Becomes: 
 * A -> B
 * 
 * @author bradsdavis
 *
 */
public class HealGoto extends GraphIntermediateVisitor {

	public HealGoto(IntermediateGraphContext igc) {
		super(igc);
	}

	
	@Override
	public void visitGoToIntermediate(GoToIntermediate line) {
		AbstractIntermediate predecessor = igc.getSinglePredecessor(line);
		AbstractIntermediate successor = igc.getSingleSuccessor(line);
		
		igc.getIntermediateGraph().removeVertex(line);
		igc.getIntermediateGraph().addEdge(predecessor, successor);
	}
}

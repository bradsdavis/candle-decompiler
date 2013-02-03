package org.candle.decompiler.intermediate.graph.enhancer;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.IntermediateEdge;
import org.jgrapht.DirectedGraph;

public class NegateConditional extends GraphIntermediateVisitor {

	public NegateConditional(DirectedGraph<AbstractIntermediate, IntermediateEdge> intermediateGraph) {
		super(intermediateGraph);
	}
	
	@Override
	public void visitConditionalLine(ConditionalIntermediate line) {
		//check the conditional to make sure the TRUE has the minimum instruction.
		int trueTarget = line.getTrueTarget().getInstruction().getPosition();
		int falseTarget = line.getFalseTarget().getInstruction().getPosition();
		
		if(falseTarget < trueTarget) {
			line.negate();
		}
	}

}

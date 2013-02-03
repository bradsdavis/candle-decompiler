package org.candle.decompiler.intermediate.graph.enhancer;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.code.conditional.IfIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.IntermediateEdge;
import org.jgrapht.DirectedGraph;

public class If extends GraphIntermediateVisitor {

	public If(DirectedGraph<AbstractIntermediate, IntermediateEdge> intermediateGraph) {
		super(intermediateGraph);
	}

	@Override
	public void visitConditionalLine(ConditionalIntermediate line) {
		
		//transform to IF block.
		
		IfIntermediate ifIntermediate = new IfIntermediate(line.getInstruction(), line.getExpression());
		ifIntermediate.setTrueTarget(line.getTrueTarget());
		ifIntermediate.setFalseTarget(line.getFalseTarget());
		
		this.intermediateGraph.addVertex(ifIntermediate);
		
		//now, replace the vertex.
		updatePredecessorConditional(line, ifIntermediate);
		redirectPredecessors(line, ifIntermediate);
		redirectSuccessors(line, ifIntermediate);
		
		this.intermediateGraph.removeVertex(line);
	}
	

}

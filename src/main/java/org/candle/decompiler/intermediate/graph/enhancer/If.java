package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.code.conditional.IfIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.IntermediateEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;

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
	
	protected void updatePredecessorConditional(ConditionalIntermediate source, ConditionalIntermediate target) {
		if(source instanceof ConditionalIntermediate) {
			List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(intermediateGraph, source);
			
			for(AbstractIntermediate predecessor : predecessors) {
				updatePredecessorConditional(predecessor, (ConditionalIntermediate)source, target);
			}
		}
	}

	protected void updatePredecessorConditional(AbstractIntermediate predecessor, ConditionalIntermediate source, ConditionalIntermediate target) {
		if(predecessor instanceof ConditionalIntermediate) {
			ConditionalIntermediate conditionalPredecessor = (ConditionalIntermediate)predecessor;
			
			if(conditionalPredecessor.getTrueTarget() == source) {
				conditionalPredecessor.setTrueTarget(target);
			}
			
			if(conditionalPredecessor.getFalseTarget() == source) {
				conditionalPredecessor.setFalseTarget(target);
			}
			
		}
	}
	
}

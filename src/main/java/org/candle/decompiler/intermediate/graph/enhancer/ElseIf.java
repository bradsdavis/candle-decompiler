package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.conditional.ElseIfIntermediate;
import org.candle.decompiler.intermediate.code.conditional.IfIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.IntermediateEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;

public class ElseIf extends GraphIntermediateVisitor {

	public ElseIf(DirectedGraph<AbstractIntermediate, IntermediateEdge> intermediateGraph) {
		super(intermediateGraph);
	}
	
	@Override
	public void visitIfLine(IfIntermediate line) {
		//check to see if the predecessor is an if block.
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(intermediateGraph, line);
		
		if(predecessors.size() != 1) {
			return;
		}
		
		//otherwise, see if it is another IF.
		if(predecessors.get(0) instanceof IfIntermediate || predecessors.get(0) instanceof ElseIfIntermediate) {
			//check to see whether it is on the ELSE side.
			IfIntermediate parent = (IfIntermediate)predecessors.get(0);
			
			if(parent.getFalseTarget() == line) {
				//then this could be an IF block.
				
				ElseIfIntermediate eii = new ElseIfIntermediate(line.getInstruction(), line.getExpression());
				eii.setTrueTarget(line.getTrueTarget());
				eii.setFalseTarget(line.getFalseTarget());
				
				this.intermediateGraph.addVertex(eii);
				
				redirectPredecessors(line, eii);
				redirectSuccessors(line, eii);
				
				this.intermediateGraph.removeVertex(line);
			}
		}
		
	}

}

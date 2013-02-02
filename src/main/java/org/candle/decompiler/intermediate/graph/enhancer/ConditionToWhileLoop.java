package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.loop.WhileIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class ConditionToWhileLoop extends GraphIntermediateVisitor {
	
	public ConditionToWhileLoop(DirectedGraph<AbstractIntermediate, DefaultEdge> intermediateGraph) {
		super(intermediateGraph);
	}

	@Override
	public void visitConditionalLine(ConditionalIntermediate line) {
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(intermediateGraph, line);
		
		//first, determine if the condition has two incoming lines.
		if(predecessors.size() == 2) {
			//check to see that 1 predecessor is a GOTO.
			
			GoToIntermediate gotoLine = null;
			AbstractIntermediate otherLine = null;
			
			//classify.
			for(AbstractIntermediate predecessor : predecessors) {
				//check to see if 1 is GOTO.
				if(predecessor instanceof GoToIntermediate) {
					gotoLine = (GoToIntermediate)predecessor;
					continue;
				}
				else {
					otherLine = predecessor;
				}
			}
			
			//stop if both conditions aren't met.
			if(gotoLine == null || otherLine == null) {
				return;
			}
			
			//check to validate that the GOTO instruction is less than the other incoming...
			if(comparator.before(gotoLine, line) && comparator.before(otherLine, line)) {
				WhileIntermediate whileIntermediate = new WhileIntermediate(gotoLine, line);
				//add this to the graph.
				this.intermediateGraph.addVertex(whileIntermediate);
				
				//get the incoming from the goto...
				redirectPredecessors(gotoLine, whileIntermediate);
				
				//now, add the reference to the condition
				this.intermediateGraph.addEdge(whileIntermediate, whileIntermediate.getTrueTarget());
				this.intermediateGraph.addEdge(whileIntermediate, whileIntermediate.getFalseTarget());
				
				//now, create line from other to while.
				this.intermediateGraph.addEdge(otherLine, whileIntermediate);
				
				if(whileIntermediate.getTrueTarget().getInstruction().getPosition() > whileIntermediate.getFalseTarget().getInstruction().getPosition()) {
					//negate.
					whileIntermediate.negate();
				}
				
				//now, remove the GOTO and Conditional Vertext from graph.
				retract(gotoLine);
				retract(line);
			}
		}
	}
	
	
}

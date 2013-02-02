package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;
import java.util.TreeSet;

import org.apache.bcel.generic.BranchHandle;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.code.loop.WhileIntermediate;
import org.candle.decompiler.intermediate.expression.Continue;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.IntermediateEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.CycleDetector;

public class ConditionToWhileLoop extends GraphIntermediateVisitor {
	
	public ConditionToWhileLoop(DirectedGraph<AbstractIntermediate, IntermediateEdge> intermediateGraph) {
		super(intermediateGraph);
	}

	@Override
	public void visitConditionalLine(ConditionalIntermediate line) {
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(intermediateGraph, line);
		
		CycleDetector<AbstractIntermediate, IntermediateEdge> cycleDetector = new CycleDetector<AbstractIntermediate, IntermediateEdge>(intermediateGraph);
		if(!cycleDetector.detectCyclesContainingVertex(line)) {
			return;
		}
		
		//first, determine if the condition has two incoming lines.
		if(predecessors.size() >= 2) {
			//check to see that 1 predecessor is a GOTO.
			
			TreeSet<GoToIntermediate> incomingGotoNonNested = new TreeSet<GoToIntermediate>();
			TreeSet<GoToIntermediate> incomingGotoNested = new TreeSet<GoToIntermediate>();
			GoToIntermediate nonNestedLine = null;
			AbstractIntermediate otherLine = null;
			
			//classify.
			for(AbstractIntermediate predecessor : predecessors) {
				//check to see if 1 is GOTO.
				if(predecessor instanceof GoToIntermediate) {
					if(isNested(line, predecessor)) {
						incomingGotoNested.add((GoToIntermediate)predecessor);
					}
					else {
						incomingGotoNonNested.add((GoToIntermediate)predecessor);
					}
					continue;
				}
				else {
					otherLine = predecessor;
				}
			}
			
			//if there are more than one GOTO statements that are not-nested, return.
			if(incomingGotoNonNested.size() > 1) {
				return;
			}
			
			nonNestedLine = incomingGotoNonNested.pollFirst();
			
			//stop if both conditions aren't met.
			if(nonNestedLine == null || otherLine == null) {
				return;
			}
			
			//check to validate that the GOTO instruction is less than the other incoming...
			if(comparator.before(nonNestedLine, line) && comparator.before(otherLine, line)) {
				WhileIntermediate whileIntermediate = new WhileIntermediate((BranchHandle)nonNestedLine.getInstruction(), line);
				//add this to the graph.
				this.intermediateGraph.addVertex(whileIntermediate);
				
				//get the incoming from the goto...
				redirectPredecessors(nonNestedLine, whileIntermediate);
				
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
				retract(nonNestedLine);
				retract(line);
				
				
				
				//now, the other GOTO lines coming in should all be CONTINUE statements...
				for(GoToIntermediate gotoIntermediate : incomingGotoNested) {
					Continue continueExpression = new Continue(gotoIntermediate.getInstruction()); 
					StatementIntermediate continueIntermediate = new StatementIntermediate(gotoIntermediate.getInstruction(), continueExpression);
					
					//add the node...
					this.intermediateGraph.addVertex(continueIntermediate);
					redirectPredecessors(gotoIntermediate, continueIntermediate);
					//remove vertex.
					this.intermediateGraph.removeVertex(gotoIntermediate);
					
					//now, add line to the loop.
					this.intermediateGraph.addEdge(continueIntermediate, whileIntermediate);
				}
			}
		}
	}

	protected boolean isNested(ConditionalIntermediate ci, AbstractIntermediate ai) 
	{
		int max = ci.getFalseTarget().getInstruction().getPosition();
		int min = ci.getTrueTarget().getInstruction().getPosition();
		
		return (ai.getInstruction().getPosition() <= max && ai.getInstruction().getPosition() >= min);
		
		
	}
	
}

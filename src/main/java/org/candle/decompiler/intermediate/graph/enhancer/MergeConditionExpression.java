package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.expression.LogicalGateConditionalExpression;
import org.candle.decompiler.intermediate.expression.LogicalGateType;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class MergeConditionExpression extends GraphIntermediateVisitor {
	
	public MergeConditionExpression(DirectedGraph<AbstractIntermediate, DefaultEdge> intermediateGraph) {
		super(intermediateGraph);
	}

	@Override
	public void visitConditionalLine(ConditionalIntermediate line) {
		List<AbstractIntermediate> successors = Graphs.successorListOf(intermediateGraph, line);
		List<AbstractIntermediate> predecessor = Graphs.predecessorListOf(intermediateGraph, line);
		
		for(AbstractIntermediate i : predecessor) {
			//check to see whether the incoming is a conditional..
			if(i instanceof ConditionalIntermediate) {
				ConditionalIntermediate ci = (ConditionalIntermediate)i;
				
				//potential to merge.
				
				if(ci == line) {
					continue;
				}
				
				//merging or statements are when conditionals have 2 legs.  in that case, if both legs target only the same target
				//and one leg of the other conditional targets this conditional, then we can compress.
				
				//we already know here that the conditional i enters this node.  check whether the outcome of this node matches the other node.
				List<AbstractIntermediate> cSuccess = Graphs.successorListOf(intermediateGraph, ci);
				
				//first, remove self from list.
				cSuccess.remove(line);
				
				//next, for each successor of this, remove from other.
				for(AbstractIntermediate ai : successors) {
					cSuccess.remove(ai);
				}
				
				//if this is empty now, we can merge.
				if(cSuccess.size() == 0) {
					System.out.println("Merge: "+ line + " AND "+i);
					
					//check to see if we need to negate first...
					if(ci.getFalseTarget() == line.getTrueTarget()) {
						ci.negate();
						System.out.println("Need CI: "+ci);
					}
					else if(ci.getTrueTarget() == line.getFalseTarget()) {
						line.negate();
						System.out.println("Negated Line: "+line);
					}
					
					if(ci.getTrueTarget() == line.getTrueTarget()) {
						System.out.println("Don't even need to negate!");
						LogicalGateConditionalExpression expression = new LogicalGateConditionalExpression(ci.getExpression(), line.getExpression(), LogicalGateType.OR);
						line.setExpression(expression);
						
						//find references to ci, redirect to line.
						redirectPredecessors(ci, line);

						//now remove vertex.
						retract(ci);
					}
				}
			}
		}
	}
}

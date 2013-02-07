package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.expression.ConditionalExpression;
import org.candle.decompiler.intermediate.expression.LogicalGateConditionalExpression;
import org.candle.decompiler.intermediate.expression.LogicalGateType;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;

public class MergeConditionExpression extends GraphIntermediateVisitor {
	
	public MergeConditionExpression(IntermediateGraphContext igc) {
		super(igc, true);
	}
	
	@Override
	public void visitConditionalLine(ConditionalIntermediate line) {
		List<AbstractIntermediate> successors = Graphs.successorListOf(igc.getIntermediateGraph(), line);
		List<AbstractIntermediate> predecessor = Graphs.predecessorListOf(igc.getIntermediateGraph(), line);
		
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
				List<AbstractIntermediate> cSuccess = Graphs.successorListOf(igc.getIntermediateGraph(), ci);
				
				//first, remove self from list.
				cSuccess.remove(line);
				
				//next, for each successor of this, remove from other.
				for(AbstractIntermediate ai : successors) {
					cSuccess.remove(ai);
				}
				
				//if this is empty now, we can merge.
				if(cSuccess.size() == 0) {
					System.out.println("Merge: "+ line + " AND "+i);
					
					//previous
					ConditionalExpression m1 = ci.getExpression();
					
					//current
					ConditionalExpression m2 = line.getExpression();
					
					//check to see if we need to negate first...
					if(igc.getFalseTarget(ci) == igc.getFalseTarget(line)) {
						LogicalGateConditionalExpression expression = new LogicalGateConditionalExpression(m1, m2, LogicalGateType.AND);
						line.setExpression(expression);
					}
					else if(igc.getFalseTarget(ci) == igc.getTrueTarget(line)) {
						m1.negate();
					
						LogicalGateConditionalExpression expression = new LogicalGateConditionalExpression(m1, m2, LogicalGateType.OR);
						line.setExpression(expression);
					}
					else {
						return;
					}
					
					
					//find references to ci, redirect to line.
					igc.redirectPredecessors(ci, line);
					
					//now remove vertex.
					igc.getIntermediateGraph().removeVertex(ci);
					
					//for each predecessor, set the target if it is a conditional.
				}
			}
		}
	}
}

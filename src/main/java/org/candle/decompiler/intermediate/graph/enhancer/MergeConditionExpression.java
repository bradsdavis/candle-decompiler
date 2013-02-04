package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.expression.LogicalGateConditionalExpression;
import org.candle.decompiler.intermediate.expression.LogicalGateType;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;

public class MergeConditionExpression extends GraphIntermediateVisitor {
	
	public MergeConditionExpression(IntermediateGraphContext igc) {
		super(igc, true);
	}
	
	protected void updatePredecessorConditional(ConditionalIntermediate source, ConditionalIntermediate target) {
		if(source instanceof ConditionalIntermediate) {
			List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(igc.getIntermediateGraph(), source);
			
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
						line.setTrueTarget(ci.getTrueTarget());
						updatePredecessorConditional(ci, line);
						
						//find references to ci, redirect to line.
						igc.redirectPredecessors(ci, line);
						
						//now remove vertex.
						igc.getIntermediateGraph().removeVertex(ci);
					}
					
					//for each predecessor, set the target if it is a conditional.
				}
			}
		}
	}
}

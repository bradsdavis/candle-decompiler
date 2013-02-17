package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.List;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchOutcome;
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
	
	protected void mergeConditions(BooleanBranchIntermediate bbo1, BooleanBranchIntermediate bbo2) {
		//check to see if the false of bbo2 and bbo1 go to same location.

		//previous
		ConditionalExpression m1 = bbo2.getExpression();
		
		//current
		ConditionalExpression m2 = bbo1.getExpression();
		
		
		if(igc.getTrueTarget(bbo2) == bbo1) {
			if(igc.getFalseTarget(bbo1) == igc.getFalseTarget(bbo2)) {
				LogicalGateConditionalExpression expression = new LogicalGateConditionalExpression(m1, m2, LogicalGateType.AND);
				bbo1.setExpression(expression);
			}
			else if(igc.getFalseTarget(bbo2) == igc.getTrueTarget(bbo1)) {
				m1.negate();
				
				LogicalGateConditionalExpression expression = new LogicalGateConditionalExpression(m1, m2, LogicalGateType.OR);
				bbo1.setExpression(expression);
			}
			else {
				return;
			}
			
			//ok, we merged?  remove bbo2.
			igc.redirectPredecessors(bbo2, bbo1);
			
			//remove vertexes of bbo2.
			igc.getIntermediateGraph().removeVertex(bbo2.getFalseBranch());
			igc.getIntermediateGraph().removeVertex(bbo2.getTrueBranch());
			igc.getIntermediateGraph().removeVertex(bbo2);
		}
	}
	
	@Override
	public void visitBooleanBranchIntermediate(BooleanBranchIntermediate bbo1) {
		List<AbstractIntermediate> predecessor = Graphs.predecessorListOf(igc.getIntermediateGraph(), bbo1);
		
		for(AbstractIntermediate i : predecessor) {
			if(!igc.getIntermediateGraph().containsVertex(i)) {
				continue;
			}
			
			//check to see whether the incoming is a conditional..
			if(i instanceof BooleanBranchOutcome) {
				BooleanBranchOutcome booleanBranchOutcome = (BooleanBranchOutcome)i;
				
				//get a reference to the parent...
				BooleanBranchIntermediate bbo2 = (BooleanBranchIntermediate)igc.getSinglePredecessor(booleanBranchOutcome);
				//ok, we have two branches coming in...
				
				mergeConditions(bbo1, bbo2);
			}
		}
	}
}

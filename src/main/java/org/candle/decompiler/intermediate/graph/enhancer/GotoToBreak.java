package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.code.loop.EnhancedForIntermediate;
import org.candle.decompiler.intermediate.code.loop.ForIntermediate;
import org.candle.decompiler.intermediate.code.loop.WhileIntermediate;
import org.candle.decompiler.intermediate.expression.Break;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.jgrapht.Graphs;

public class GotoToBreak extends GraphIntermediateVisitor {

	public GotoToBreak(IntermediateGraphContext igc) {
		super(igc, false);
	}
	
	
	@Override
	public void visitWhileLoopLine(WhileIntermediate line) {
		processLoop(line);
	}

	@Override
	public void visitForLoopLine(ForIntermediate line) {
		processLoop(line);
	}
	
	@Override
	public void visitEnhancedForLoopLine(EnhancedForIntermediate line) {
		processLoop(line);
	}
	
	public void processLoop(WhileIntermediate loop) {
		//get the target... then look for GOTO statements going into the target that are within the range 
		//of the loop.
		TreeSet<AbstractIntermediate> loopElements = (TreeSet<AbstractIntermediate>)igc.getOrderedIntermediate().subSet(igc.getTrueTarget(loop), true, igc.getFalseTarget(loop), false);
		
		
		List<AbstractIntermediate> predecessors = Graphs.predecessorListOf(igc.getIntermediateGraph(), igc.getFalseTarget(loop));
		
		Set<GoToIntermediate> gotoToBreak = new HashSet<GoToIntermediate>();
		for(AbstractIntermediate predecessor : predecessors) 
		{
			if(predecessor instanceof GoToIntermediate) 
			{
				if(loopElements.contains(predecessor)) {
					gotoToBreak.add((GoToIntermediate)predecessor);
				}
			}
		}
		
		//now, we create new statements.
		for(GoToIntermediate gotoToBreakStatement : gotoToBreak) {
			transformGotoToBreak(gotoToBreakStatement);
		}
	}
	
	public void transformGotoToBreak(GoToIntermediate gotoStatement) {
		Break breakExpression = new Break(gotoStatement.getInstruction());
		StatementIntermediate breakStatement = new StatementIntermediate(gotoStatement.getInstruction(), breakExpression);
		
		igc.getIntermediateGraph().addVertex(breakStatement);
		igc.redirectPredecessors(gotoStatement, breakStatement);
		igc.redirectSuccessors(gotoStatement, breakStatement);
		
		igc.getIntermediateGraph().removeVertex(gotoStatement);
	}
	
}

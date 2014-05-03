package org.candle.decompiler.intermediate.graph.enhancer;

import java.util.TreeSet;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.code.SwitchIntermediate;
import org.candle.decompiler.intermediate.expression.Break;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;
import org.candle.decompiler.intermediate.graph.edge.SwitchEdge;

public class SwitchGotoToBreak extends GraphIntermediateVisitor {

	public SwitchGotoToBreak(IntermediateGraphContext igc) {
		super(igc, false);
	}
	
	
	@Override
	public void visitSwitchIntermediate(SwitchIntermediate line) {

		if(igc.getDefaultCase(line)!=null) {
			SwitchEdge se = igc.getDefaultCase(line);
			
			AbstractIntermediate defaultNode = se.getTargetIntermediate(); 
			TreeSet<AbstractIntermediate> elements = (TreeSet<AbstractIntermediate>)igc.getOrderedIntermediate().subSet(line, true, defaultNode, false);
			
			int position = defaultNode.getInstruction().getPosition();
			
			
			//look for goto statements...
			for(AbstractIntermediate element : elements) {
				if(element instanceof GoToIntermediate) {
					GoToIntermediate gti = (GoToIntermediate)element;
					
					if(igc.getTarget(gti).getInstruction().getPosition() > position) {
						transformGotoToBreak(gti);
					}
				}
			}
		}
	}
	
	public void transformGotoToBreak(GoToIntermediate gotoStatement) {
		Break breakExpression = new Break(gotoStatement.getInstruction());
		StatementIntermediate breakStatement = new StatementIntermediate(gotoStatement.getInstruction(), breakExpression);
		
		igc.getGraph().addVertex(breakStatement);
		igc.redirectPredecessors(gotoStatement, breakStatement);
		igc.redirectSuccessors(gotoStatement, breakStatement);
		
		igc.getGraph().removeVertex(gotoStatement);
	}
	
}

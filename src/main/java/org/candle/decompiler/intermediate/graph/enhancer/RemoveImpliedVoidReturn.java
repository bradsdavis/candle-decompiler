package org.candle.decompiler.intermediate.graph.enhancer;

import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.expression.Return;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;

/**
 * Removes the return statement in a void method, where the statement is the last statement of the method.
 * @author bradsdavis
 *
 */
public class RemoveImpliedVoidReturn extends GraphIntermediateVisitor {

	public RemoveImpliedVoidReturn(IntermediateGraphContext igc) {
		super(igc);
	}
	
	@Override
	public void visitStatementIntermediate(StatementIntermediate line) {
		//find out if this is the last statement...
		if(line.getExpression() instanceof Return) {
			Return returnStatement = (Return)line.getExpression();
			
			//check to see if it is returning void.
			if(returnStatement.getChild() == null) {
				//is this the last statement?
				if(line == igc.getOrderedIntermediate().last()) {
					igc.getGraph().removeVertex(line);
				}
			}
		}
		
	}

}

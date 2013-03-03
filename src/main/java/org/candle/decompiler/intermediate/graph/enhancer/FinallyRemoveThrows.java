package org.candle.decompiler.intermediate.graph.enhancer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.FinallyIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.expression.Throw;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;

public class FinallyRemoveThrows extends GraphIntermediateVisitor {
	private static final Log LOG = LogFactory.getLog(FinallyRemoveThrows.class);
	
	public FinallyRemoveThrows(IntermediateGraphContext igc) {
		super(igc);
	}
	
	@Override
	public void visitFinallyIntermediate(FinallyIntermediate line) {
		//get the last node...
		if(true)return;
		
		if(line.getBlockRange().getEnd() != null);
		{
			AbstractIntermediate ai = igc.findPreviousNode(line.getBlockRange().getEnd());
			if(ai instanceof StatementIntermediate) {
				if(((StatementIntermediate) ai).getExpression() instanceof Throw)
				{
					LOG.debug("Removing throw to finalize prototype of finally.");
					igc.getIntermediateGraph().removeVertex(ai);
				}
			}
		}
	}
}

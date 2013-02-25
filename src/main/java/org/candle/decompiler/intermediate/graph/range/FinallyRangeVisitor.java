package org.candle.decompiler.intermediate.graph.range;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.FinallyIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.expression.Throw;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;

public class FinallyRangeVisitor extends GraphIntermediateVisitor {

	public FinallyRangeVisitor(IntermediateGraphContext igc) {
		super(igc);
	}

	@Override
	public void visitFinallyIntermediate(FinallyIntermediate line) {
		NonGotoIterator iter = new NonGotoIterator(igc.getIntermediateGraph(), line);
		//walk until we find a THROW.
		
		AbstractIntermediate throwsStatement = null;
		while(iter.hasNext()) {
			AbstractIntermediate i = iter.next();
			if(i instanceof StatementIntermediate) {
				StatementIntermediate s = (StatementIntermediate)i;
				
				if(s.getExpression() instanceof Throw) {
					throwsStatement = s;
					break;
				}
			}
		}
		
		if(throwsStatement != null) {
			line.getBlockRange().setEnd(throwsStatement.getInstruction());
		}
		
	}
}

package org.candle.decompiler.intermediate.graph.range;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.loop.EnhancedForIntermediate;
import org.candle.decompiler.intermediate.code.loop.ForIntermediate;
import org.candle.decompiler.intermediate.code.loop.WhileIntermediate;
import org.candle.decompiler.intermediate.graph.GraphIntermediateVisitor;
import org.candle.decompiler.intermediate.graph.context.IntermediateGraphContext;

public class WhileRangeVisitor extends GraphIntermediateVisitor {

	
	public WhileRangeVisitor(IntermediateGraphContext igc) {
		super(igc, false);
	}

	@Override
	public void visitWhileIntermediate(WhileIntermediate line) {
		AbstractIntermediate falseTarget = igc.getFalseTarget(line);
		AbstractIntermediate trueTarget = igc.getTrueTarget(line);
		
		line.getBlockRange().setStart(trueTarget.getInstruction());
		line.getBlockRange().setEnd(falseTarget.getInstruction().getPrev());
		super.visitWhileIntermediate(line);
	}
	
	@Override
	public void visitForIntermediate(ForIntermediate line) {
		this.visitWhileIntermediate(line);
	}
	
	@Override
	public void visitEnhancedForLoopIntermediate(EnhancedForIntermediate line) {
		this.visitWhileIntermediate(line);
	}
}

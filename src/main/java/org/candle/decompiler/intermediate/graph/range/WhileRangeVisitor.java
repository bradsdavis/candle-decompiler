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
	public void visitWhileLoopLine(WhileIntermediate line) {
		AbstractIntermediate falseTarget = igc.getFalseTarget(line);
		AbstractIntermediate trueTarget = igc.getTrueTarget(line);
		
		line.getBlockRange().setStart(trueTarget.getInstruction().getPosition());
		line.getBlockRange().setEnd(falseTarget.getInstruction().getPosition());
		super.visitWhileLoopLine(line);
	}
	
	@Override
	public void visitForLoopLine(ForIntermediate line) {
		this.visitWhileLoopLine(line);
	}
	
	@Override
	public void visitEnhancedForLoopLine(EnhancedForIntermediate line) {
		this.visitWhileLoopLine(line);
	}
}

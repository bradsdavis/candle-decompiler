package org.candle.decompiler.intermediate.visitor;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.code.loop.EnhancedForIntermediate;
import org.candle.decompiler.intermediate.code.loop.ForIntermediate;
import org.candle.decompiler.intermediate.code.loop.WhileIntermediate;

public class EmptyIntermediateVisitor implements IntermediateVisitor {

	@Override
	public void visitForLoopLine(ForIntermediate line) {

	}

	@Override
	public void visitCompleteLine(StatementIntermediate line) {
		
	}

	@Override
	public void visitGoToLine(GoToIntermediate line) {
		
	}

	@Override
	public void visitConditionalLine(ConditionalIntermediate line) {
		
	}

	@Override
	public void visitAbstractLine(AbstractIntermediate line) {
		
	}

	@Override
	public void visitWhileLoopLine(WhileIntermediate line) {
		
	}

	@Override
	public void visitEnhancedForLoopLine(EnhancedForIntermediate line) {
		
	}

}

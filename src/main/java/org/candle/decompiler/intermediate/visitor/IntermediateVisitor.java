package org.candle.decompiler.intermediate.visitor;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.code.loop.ForIntermediate;
import org.candle.decompiler.intermediate.code.loop.WhileIntermediate;

public interface IntermediateVisitor {
	public void visitForLoopLine(ForIntermediate line);
	public void visitWhileLoopLine(WhileIntermediate line);
	
	public void visitCompleteLine(StatementIntermediate line);
	public void visitGoToLine(GoToIntermediate line);
	public void visitConditionalLine(ConditionalIntermediate line);
	public void visitAbstractLine(AbstractIntermediate line);
	
}

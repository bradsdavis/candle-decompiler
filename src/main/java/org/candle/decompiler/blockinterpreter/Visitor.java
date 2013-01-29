package org.candle.decompiler.blockinterpreter;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;

public interface Visitor {

	public void visitCompleteLine(StatementIntermediate line);
	public void visitGoToLine(GoToIntermediate line);
	public void visitConditionalLine(ConditionalIntermediate line);
	public void visitAbstractLine(AbstractIntermediate line);
	
}

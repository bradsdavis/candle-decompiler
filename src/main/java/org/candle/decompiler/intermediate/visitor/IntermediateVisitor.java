package org.candle.decompiler.intermediate.visitor;

import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.code.BooleanBranchOutcome;
import org.candle.decompiler.intermediate.code.CaseIntermediate;
import org.candle.decompiler.intermediate.code.CatchIntermediate;
import org.candle.decompiler.intermediate.code.FinallyIntermediate;
import org.candle.decompiler.intermediate.code.GoToIntermediate;
import org.candle.decompiler.intermediate.code.MultiBranchIntermediate;
import org.candle.decompiler.intermediate.code.StatementIntermediate;
import org.candle.decompiler.intermediate.code.TryIntermediate;
import org.candle.decompiler.intermediate.code.conditional.ElseIfIntermediate;
import org.candle.decompiler.intermediate.code.conditional.ElseIntermediate;
import org.candle.decompiler.intermediate.code.conditional.IfIntermediate;
import org.candle.decompiler.intermediate.code.loop.EnhancedForIntermediate;
import org.candle.decompiler.intermediate.code.loop.ForIntermediate;
import org.candle.decompiler.intermediate.code.loop.WhileIntermediate;

public interface IntermediateVisitor {
	//branch pseudo
	public void visitBooleanBranchOutcome(BooleanBranchOutcome line);
	
	public void visitEnhancedForLoopLine(EnhancedForIntermediate line);
	public void visitForLoopLine(ForIntermediate line);
	public void visitWhileLoopLine(WhileIntermediate line);
	
	public void visitTryIntermediate(TryIntermediate line);
	public void visitFinallyIntermediate(FinallyIntermediate line);
	public void visitCatchLine(CatchIntermediate line);
	public void visitCaseLine(CaseIntermediate line);
	
	
	public void visitIfLine(IfIntermediate line);
	public void visitElseIfLine(ElseIfIntermediate line);
	public void visitElseLine(ElseIntermediate line);
	
	
	public void visitCompleteLine(StatementIntermediate line);
	public void visitGoToLine(GoToIntermediate line);
	public void visitMultiConditionalLine(MultiBranchIntermediate line);
	public void visitBooleanBranchIntermediate(BooleanBranchIntermediate line);
	public void visitAbstractLine(AbstractIntermediate line);
	
}

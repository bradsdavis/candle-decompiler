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
import org.candle.decompiler.intermediate.code.SwitchIntermediate;
import org.candle.decompiler.intermediate.code.TryIntermediate;
import org.candle.decompiler.intermediate.code.conditional.ElseIfIntermediate;
import org.candle.decompiler.intermediate.code.conditional.ElseIntermediate;
import org.candle.decompiler.intermediate.code.conditional.IfIntermediate;
import org.candle.decompiler.intermediate.code.loop.ContinuousWhileIntermediate;
import org.candle.decompiler.intermediate.code.loop.EnhancedForIntermediate;
import org.candle.decompiler.intermediate.code.loop.ForIntermediate;
import org.candle.decompiler.intermediate.code.loop.WhileIntermediate;

public interface IntermediateVisitor {
	//branch pseudo
	public void visitBooleanBranchOutcome(BooleanBranchOutcome line);
	
	public void visitEnhancedForLoopIntermediate(EnhancedForIntermediate line);
	public void visitForIntermediate(ForIntermediate line);
	public void visitWhileIntermediate(WhileIntermediate line);
	public void visitContinuousWhileIntermediate(ContinuousWhileIntermediate line);
	
	public void visitTryIntermediate(TryIntermediate line);
	public void visitFinallyIntermediate(FinallyIntermediate line);
	public void visitCatchIntermediate(CatchIntermediate line);
	public void visitCaseIntermediate(CaseIntermediate line);
	
	public void visitSwitchIntermediate(SwitchIntermediate line);
	
	public void visitIfIntermediate(IfIntermediate line);
	public void visitElseIfIntermediate(ElseIfIntermediate line);
	public void visitElseIntermediate(ElseIntermediate line);
	
	
	public void visitStatementIntermediate(StatementIntermediate line);
	public void visitGoToIntermediate(GoToIntermediate line);
	public void visitMultiBranchIntermediate(MultiBranchIntermediate line);
	public void visitBooleanBranchIntermediate(BooleanBranchIntermediate line);
	public void visitAbstractIntermediate(AbstractIntermediate line);
	
	
}

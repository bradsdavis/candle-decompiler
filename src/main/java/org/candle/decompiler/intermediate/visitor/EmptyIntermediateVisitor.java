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
import org.candle.decompiler.intermediate.code.loop.EnhancedForIntermediate;
import org.candle.decompiler.intermediate.code.loop.ForIntermediate;
import org.candle.decompiler.intermediate.code.loop.WhileIntermediate;

public class EmptyIntermediateVisitor implements IntermediateVisitor {

	@Override
	public void visitForIntermediate(ForIntermediate line) {

	}

	@Override
	public void visitStatementIntermediate(StatementIntermediate line) {
		
	}

	@Override
	public void visitGoToIntermediate(GoToIntermediate line) {
		
	}

	@Override
	public void visitBooleanBranchIntermediate(BooleanBranchIntermediate line) {
		
	}

	@Override
	public void visitAbstractIntermediate(AbstractIntermediate line) {
		
	}

	@Override
	public void visitWhileIntermediate(WhileIntermediate line) {
		
	}

	@Override
	public void visitEnhancedForLoopIntermediate(EnhancedForIntermediate line) {
		
	}

	@Override
	public void visitIfIntermediate(IfIntermediate line) {
		
	}

	@Override
	public void visitElseIfIntermediate(ElseIfIntermediate line) {
		
	}
	
	@Override
	public void visitElseIntermediate(ElseIntermediate line) {
		
	}

	@Override
	public void visitMultiBranchIntermediate(MultiBranchIntermediate line) {
		
	}

	@Override
	public void visitCaseIntermediate(CaseIntermediate line) {
		
	}

	@Override
	public void visitBooleanBranchOutcome(BooleanBranchOutcome line) {
		
	}

	@Override
	public void visitCatchIntermediate(CatchIntermediate line) {
		
	}

	@Override
	public void visitTryIntermediate(TryIntermediate line) {
		
	}

	@Override
	public void visitFinallyIntermediate(FinallyIntermediate line) {
		
	}

	@Override
	public void visitSwitchIntermediate(SwitchIntermediate line) {
		
	}

}

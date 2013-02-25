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
	public void visitBooleanBranchIntermediate(BooleanBranchIntermediate line) {
		
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

	@Override
	public void visitIfLine(IfIntermediate line) {
		
	}

	@Override
	public void visitElseIfLine(ElseIfIntermediate line) {
		
	}
	
	@Override
	public void visitElseLine(ElseIntermediate line) {
		
	}

	@Override
	public void visitMultiConditionalLine(MultiBranchIntermediate line) {
		
	}

	@Override
	public void visitCaseLine(CaseIntermediate line) {
		
	}

	@Override
	public void visitBooleanBranchOutcome(BooleanBranchOutcome line) {
		
	}

	@Override
	public void visitCatchLine(CatchIntermediate line) {
		
	}

	@Override
	public void visitTryIntermediate(TryIntermediate line) {
		
	}

	@Override
	public void visitFinallyIntermediate(FinallyIntermediate line) {
		
	}

}

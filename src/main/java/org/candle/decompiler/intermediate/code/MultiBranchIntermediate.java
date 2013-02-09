package org.candle.decompiler.intermediate.code;

import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.expression.Case;
import org.candle.decompiler.intermediate.expression.Switch;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class MultiBranchIntermediate extends AbstractIntermediate {

	private final Switch expression;
	private final Case defaultCase;
	private final Set<Case> cases;
	
	public MultiBranchIntermediate(InstructionHandle instruction, Switch expression, Case defaultCase, Set<Case> cases) {
		super(instruction);
		
		this.defaultCase = defaultCase;
		this.cases = cases;
		this.expression = expression;
	}
	
	public Case getDefaultCase() {
		return defaultCase;
	}
	
	public Set<Case> getCases() {
		return cases;
	}
	
	public Switch getExpression() {
		return expression;
	}

	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitMultiConditionalLine(this);
	}
}

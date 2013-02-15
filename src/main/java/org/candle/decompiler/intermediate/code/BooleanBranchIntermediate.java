package org.candle.decompiler.intermediate.code;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.expression.ConditionalExpression;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class BooleanBranchIntermediate extends AbstractIntermediate {

	protected ConditionalExpression expression;
	protected BooleanBranchOutcome trueBranch;
	protected BooleanBranchOutcome falseBranch;
	
	public BooleanBranchIntermediate(InstructionHandle instruction, ConditionalExpression expression) {
		super(instruction);
		this.expression = expression;
	}

	public void setTrueBranch(BooleanBranchOutcome trueBranch) {
		this.trueBranch = trueBranch;
	}
	
	public void setFalseBranch(BooleanBranchOutcome falseBranch) {
		this.falseBranch = falseBranch;
	}
	
	public BooleanBranchOutcome getTrueBranch() {
		return trueBranch;
	}
	
	public BooleanBranchOutcome getFalseBranch() {
		return falseBranch;
	}
	
	protected void negate() {
		expression.negate();
	}
	
	public ConditionalExpression getExpression() {
		return expression;
	}
	
	public void setExpression(ConditionalExpression expression) {
		this.expression = expression;
	}
	
	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		try {
			this.expression.write(sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Conditional: "+sw.toString();
	}

	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitAbstractLine(this);
		visitor.visitBooleanBranchIntermediate(this);
	}
}

package org.candle.decompiler.intermediate.code;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.expression.ConditionalExpression;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class BooleanBranchIntermediate extends AbstractIntermediate {

	protected ConditionalExpression expression;
	
	public BooleanBranchIntermediate(InstructionHandle instruction, ConditionalExpression expression) {
		super(instruction);
		this.expression = expression;
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
		visitor.visitAbstractIntermediate(this);
		visitor.visitBooleanBranchIntermediate(this);
	}
}

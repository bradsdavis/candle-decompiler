package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class ArrayLength extends Expression {

	private Expression expression;
	
	public ArrayLength(InstructionHandle instructionHandle, Expression arrayTarget) {
		super(instructionHandle);
		this.expression = arrayTarget;
	}
	
	@Override
	public void write(Writer builder) throws IOException {
		expression.write(builder);
		builder.append(".");
		builder.append("length");
	}
	
	public Expression getExpression() {
		return expression;
	}
	
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
	
	@Override
	public void visit(ASTListener listener) {
		listener.accept(this);
		listener.accept(expression);
	}
}

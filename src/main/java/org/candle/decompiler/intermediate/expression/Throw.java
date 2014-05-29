package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class Throw extends Expression {

	private Expression expression;
	
	public Throw(InstructionHandle handle, Expression expression) {
		super(handle);
		this.expression = expression;
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
		listener.accept(getExpression());
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.append("throw ");
		expression.write(writer);
	}
}

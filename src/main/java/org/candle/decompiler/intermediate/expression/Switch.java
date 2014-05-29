package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class Switch extends Expression {

	private Expression expression;
	
	public Switch(InstructionHandle instructionHandle, Expression expression) {
		super(instructionHandle);
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
		if(getExpression() != null) {
			listener.accept(getExpression());
		}
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.write("switch(");
		expression.write(writer);
		writer.append(")");
	}
}

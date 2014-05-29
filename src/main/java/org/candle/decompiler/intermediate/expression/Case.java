package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class Case extends Expression {

	private InstructionHandle target;
	private Expression expression;
	
	public Case(InstructionHandle instructionHandle, InstructionHandle target, Expression expression) {
		super(instructionHandle);
		this.target = target;
		this.expression = expression;
		
	}
	
	public InstructionHandle getTarget() {
		return target;
	}
	
	public Expression getExpression() {
		return expression;
	}
	
	public void setTarget(InstructionHandle target) {
		this.target = target;
	}
	
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.append("case ");
		expression.write(writer);
		writer.append(":");
	}

	@Override
	public void visit(ASTListener listener) {
		listener.accept(this);
		listener.accept(expression);
	}
	
}

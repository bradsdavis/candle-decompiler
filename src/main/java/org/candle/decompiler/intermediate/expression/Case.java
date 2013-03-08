package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class Case extends Expression {

	private final InstructionHandle target;
	private final Expression expression;
	
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
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.append("case ");
		expression.write(writer);
		writer.append(":");
	}

}

package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class Case extends Expression {
	private final Expression expression;
	
	/***
	 * @param instructionHandle - Handle where the case begins.
	 * @param expression - Expression for the case.
	 */
	public Case(InstructionHandle instructionHandle, Expression expression) {
		super(instructionHandle);
		this.expression = expression;
		
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

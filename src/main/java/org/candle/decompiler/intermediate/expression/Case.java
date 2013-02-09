package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;

public class Case extends Expression {

	private final Expression expression;
	
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

	@Override
	public Set<Expression> nestedExpression() {
		// TODO Auto-generated method stub
		return null;
	}

}

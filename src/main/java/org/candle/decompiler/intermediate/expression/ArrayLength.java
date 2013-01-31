package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;

public class ArrayLength extends Expression {

	private final Expression arrayTarget;
	
	public ArrayLength(InstructionHandle instructionHandle, Expression arrayTarget) {
		super(instructionHandle);
		this.arrayTarget = arrayTarget;
	}
	
	@Override
	public void write(Writer builder) throws IOException {
		arrayTarget.write(builder);
		builder.append(".");
		builder.append("length");
	}
	
	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		expressions.add(arrayTarget);
		return expressions;
	}
}

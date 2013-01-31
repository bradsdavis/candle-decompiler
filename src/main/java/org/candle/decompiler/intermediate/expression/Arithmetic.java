package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;

public class Arithmetic extends Expression {

	private final ArithmeticType operation;
	private final Expression left;
	private final Expression right;
	
	public Arithmetic(InstructionHandle instructionHandle, Expression left, Expression right, ArithmeticType operation) {
		super(instructionHandle);
		this.left = left;
		this.right = right;
		this.operation = operation;
	}
	
	@Override
	public void write(Writer builder) throws IOException {
		left.write(builder);
		builder.append(" ").append(operation.toString()).append(" ");
		right.write(builder);
	}

	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		expressions.add(left);
		expressions.add(right);
		
		return expressions;
	}
	
}

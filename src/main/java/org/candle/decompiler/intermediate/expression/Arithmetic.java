package org.candle.decompiler.intermediate.expression;

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
	public String generateSource() {
		StringBuilder val = new StringBuilder(left.generateSource());
		val.append(" ").append(operation).append(" ");
		val.append(right.generateSource());

		return val.toString();
	}

	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		expressions.add(left);
		expressions.add(right);
		
		return expressions;
	}
	
}

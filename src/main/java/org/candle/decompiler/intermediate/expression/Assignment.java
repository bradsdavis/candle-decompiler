package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;


public class Assignment extends Expression {

	private final Expression left;
	private final Expression right;
	
	public Assignment(InstructionHandle instructionHandle, Expression left, Expression right) {
		super(instructionHandle);
		this.left = left;
		this.right = right;
	}
	
	public Expression getLeft() {
		return left;
	}
	
	public Expression getRight() {
		return right;
	}
	
	@Override
	public String generateSource() {
		StringBuilder builder = new StringBuilder(left.generateSource());
		builder.append(" = ");
		builder.append(right.generateSource());
		
		return builder.toString();
	}
	
	@Override
	public String toString() {
		return generateSource();
	}

	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		expressions.add(left);
		expressions.add(right);
		
		return expressions;
	}
}

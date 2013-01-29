package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;

public class InstanceOf extends Expression {

	private final Expression left;
	private final Expression right;
	
	public InstanceOf(InstructionHandle instructionHandle, Expression left, Expression right) {
		super(instructionHandle);
		this.left = left;
		this.right = right;
	}
	
	
	
	@Override
	public String generateSource() {
		StringBuilder val = new StringBuilder(left.generateSource());
		val.append(" ").append("instanceof").append(" ");
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

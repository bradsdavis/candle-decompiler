package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;


public class Cast extends Expression {
	private final Expression left;
	private final Expression right;
	
	public Cast(InstructionHandle instructionHandle, Expression left, Expression right) {
		super(instructionHandle);
		this.left = left;
		this.right = right;
	}
	
	@Override
	public String generateSource() {
		StringBuilder val = new StringBuilder();
		
		val.append("(");
		val.append(left.generateSource());
		val.append(") ");
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

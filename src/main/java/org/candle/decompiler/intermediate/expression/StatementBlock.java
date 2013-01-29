package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;

public class StatementBlock extends Expression {
	private final Expression left;
	private final Expression right;
	
	public StatementBlock(InstructionHandle instructionHandle, Expression left, Expression right) {
		super(instructionHandle);
		this.left = left;
		this.right = right;
	}
	
	@Override
	public String generateSource() {
		StringBuilder val = new StringBuilder();
		val.append(left.generateSource());
		val.append("\n");
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


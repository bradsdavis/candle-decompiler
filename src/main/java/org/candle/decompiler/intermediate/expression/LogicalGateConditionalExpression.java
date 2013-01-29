package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.Set;

public class LogicalGateConditionalExpression extends ConditionalExpression {

	private LogicalGateType logicalGate;
	private final ConditionalExpression left;
	private final ConditionalExpression right;
	
	public LogicalGateConditionalExpression(ConditionalExpression left, ConditionalExpression right, LogicalGateType logicalGate) {
		super(left.getInstructionHandle());
		this.left = left;
		this.right = right;
		this.logicalGate = logicalGate;
	}
	
	@Override
	public String generateSource() {
		StringBuilder builder = new StringBuilder();
		builder.append(left.generateSource());
		
		builder.append(" ");
		builder.append(logicalGate.toString());
		builder.append(" ");
		
		builder.append(right.generateSource());
		return builder.toString();
	}

	@Override
	public void negate() {
		switch (logicalGate) {
		case AND:
			//if you are negating and AND...
			//boolean algebra.
			// A && B negated == !A || !B
			left.negate();
			right.negate();
			logicalGate = LogicalGateType.OR;
			break;
		case OR: 
			//boolean algebra.
			// A || B negated == !A && !B
			left.negate();
			right.negate();
			logicalGate = LogicalGateType.AND;
			break;
		}
	}

	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		expressions.add(left);
		expressions.add(right);
		
		return expressions;
	}
}

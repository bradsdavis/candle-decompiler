package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

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
	public void write(Writer builder) throws IOException {
		left.write(builder);
		
		builder.append(" ");
		builder.append(logicalGate.toString());
		builder.append(" ");
		
		right.write(builder);
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

}

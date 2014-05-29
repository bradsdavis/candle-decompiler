package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

public class LogicalGateConditionalExpression extends ConditionalExpression {

	private LogicalGateType logicalGate;
	private ConditionalExpression left;
	private ConditionalExpression right;
	
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
	
	public ConditionalExpression getLeft() {
		return left;
	}
	
	public ConditionalExpression getRight() {
		return right;
	}
	
	public void setLeft(ConditionalExpression left) {
		this.left = left;
	}
	
	public void setRight(ConditionalExpression right) {
		this.right = right;
	}
	
	public LogicalGateType getLogicalGate() {
		return logicalGate;
	}
	
	public void setLogicalGate(LogicalGateType logicalGate) {
		this.logicalGate = logicalGate;
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
	public void visit(ASTListener listener) {
		listener.accept(this);
		listener.accept(getLeft());
		listener.accept(getRight());
	}

}

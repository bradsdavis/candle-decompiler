package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;

public class NewArrayInstance extends NewInstance {

	private final Expression count;
	
	public NewArrayInstance(InstructionHandle instructionHandle, String type, Expression count) {
		super(instructionHandle, type);
		this.count = count;
	}

	@Override
	public String generateSource() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.generateSource());
		builder.append("[");
		builder.append(count.generateSource());
		builder.append("]");
		
		return builder.toString();
	}
	

	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		expressions.add(count);
		return expressions;
	}
	
}

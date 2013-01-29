package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;

public class Throw extends Expression {

	private Expression throwing;
	
	public Throw(InstructionHandle handle, Expression target) {
		super(handle);
		this.throwing = target;
	}
	
	@Override
	public String generateSource() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("throw ");
		builder.append(throwing.generateSource());
		
		return builder.toString();
	}

	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>();
		expressions.add(throwing);
		
		return expressions;
	}

}

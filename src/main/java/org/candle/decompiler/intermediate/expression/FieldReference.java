package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;


public class FieldReference extends Expression {
	protected final Expression target;
	protected final String fieldName;

	public FieldReference(InstructionHandle instructionHandle, Expression target, String fieldName) {
		super(instructionHandle);
		this.target = target;
		this.fieldName = fieldName;
	}

	@Override
	public String generateSource() {
		StringBuilder builder = new StringBuilder();
		builder.append(target.generateSource());
		builder.append(".");
		builder.append(fieldName);
		
		return builder.toString();
	}
	
	@Override
	public String toString() {
		return generateSource();
	}
	
	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		expressions.add(target);
		
		return expressions;
	}
	
}

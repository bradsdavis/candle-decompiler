package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;

public class ArrayPositionReference extends Expression {

	private final Expression arrayReference;
	private final Expression arrayPosition;
	
	public ArrayPositionReference(InstructionHandle instructionHandle, Expression arrayReference, Expression arrayPosition) {
		super(instructionHandle);
		this.arrayReference = arrayReference;
		this.arrayPosition = arrayPosition;
	}

	@Override
	public String generateSource() {
		StringBuilder builder = new StringBuilder();
		builder.append(arrayReference.generateSource());
		builder.append("[");
		builder.append(arrayPosition.generateSource());
		builder.append("]");
		
		return builder.toString();
	}
	
	@Override
	public String toString() {
		return generateSource();
	}
	
	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		expressions.add(arrayPosition);
		expressions.add(arrayReference);
		
		return expressions;
	}
}

package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;


public class Resolved extends Expression {

	private final String value;
	
	public Resolved(InstructionHandle instructionHandle, String value) {
		super(instructionHandle); 
		this.value = value;
	}
	
	public String generateSource() {
		return this.value;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return generateSource();
	}
	
	@Override
	public Set<Expression> nestedExpression() {
		System.out.println("Calling nested on RESOLVED!");
		Set<Expression> expressions = new HashSet<Expression>(2);
		return expressions;
	}
}

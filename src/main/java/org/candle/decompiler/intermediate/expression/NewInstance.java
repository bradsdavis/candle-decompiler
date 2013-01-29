package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;


public class NewInstance extends ObjectType {

	public NewInstance(InstructionHandle instructionHandle, String type) {
		super(instructionHandle, type);
	}
	
	@Override
	public String generateSource() {
		StringBuilder val = new StringBuilder("new ");
		val.append(type);
		return val.toString();
	}
	
	@Override
	public String toString() {
		return generateSource();
	}
	

	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		return expressions;
	}

}

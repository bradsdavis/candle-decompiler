package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;


public class Return extends Expression {

	private Expression child = null;
	
	public Return(InstructionHandle instructionHandle, Expression child) {
		super(instructionHandle);
		this.child = child;
	}
	
	public Return(InstructionHandle instructionHandle) {
		super(instructionHandle);
	}
	
	public String generateSource() {
		StringBuilder expressionBuilder = new StringBuilder();
		expressionBuilder.append("return");
		if(child != null) {
			expressionBuilder.append(" ");
			expressionBuilder.append(child.generateSource());
		}
		
		return expressionBuilder.toString();
	}
	
	public Expression getChild() {
		return child;
	}

	@Override
	public String toString() {
		return generateSource();
	}
	
	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		if(child!=null) {
			expressions.add(child);
		}
		return expressions;
	}
}

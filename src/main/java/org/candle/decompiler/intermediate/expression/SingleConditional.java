package org.candle.decompiler.intermediate.expression;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;

public class SingleConditional extends ConditionalExpression {

	private final Expression left;
	private boolean negated;
	
	public SingleConditional(InstructionHandle instructionHandle, Expression left, boolean negated) {
		super(instructionHandle);
		this.left = left;
		this.negated = negated;
	}
	
	public SingleConditional(InstructionHandle instructionHandle, Expression left) {
		this(instructionHandle, left, false);
	}

	@Override
	public String generateSource() {
		StringBuilder val = new StringBuilder();
		
		//check to see whether to negate... 
		if(negated) {
			val.append("!(");
		}

		val.append(left.generateSource());
		
		//close parentheses around negate...
		if(negated) {
			val.append(")");
		}
		return val.toString();
	}

	@Override
	public void negate() {
		this.negated = !negated;
	}
	

	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		if(left!=null) {
			expressions.add(left);
		}
		return expressions;
	}
	
}

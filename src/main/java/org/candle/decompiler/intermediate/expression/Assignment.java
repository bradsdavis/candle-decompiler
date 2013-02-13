package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;


public class Assignment extends Expression {

	private Expression left;
	private Expression right;
	
	public Assignment(InstructionHandle instructionHandle, Expression left, Expression right) {
		super(instructionHandle);
		this.left = left;
		this.right = right;
	}
	
	public Expression getLeft() {
		return left;
	}
	
	public Expression getRight() {
		return right;
	}
	
	public void setLeft(Expression left) {
		this.left = left;
	}
	
	public void setRight(Expression right) {
		this.right = right;
	}
	
	@Override
	public void write(Writer builder) throws IOException {
		left.write(builder);
		builder.append(" = ");
		right.write(builder);
	}

	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		expressions.add(left);
		expressions.add(right);
		
		return expressions;
	}
}

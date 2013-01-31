package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;

public class InstanceOf extends Expression {

	private final Expression left;
	private final Expression right;
	
	public InstanceOf(InstructionHandle instructionHandle, Expression left, Expression right) {
		super(instructionHandle);
		this.left = left;
		this.right = right;
	}
	
	
	
	@Override
	public void write(Writer val) throws IOException {
		left.write(val);
		val.append(" ").append("instanceof").append(" ");
		right.write(val);
	}

	@Override
	public Set<Expression> nestedExpression() {
		Set<Expression> expressions = new HashSet<Expression>(2);
		expressions.add(left);
		expressions.add(right);
		
		return expressions;
	}
}

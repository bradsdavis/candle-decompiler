package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

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
	
	public Expression getLeft() {
		return left;
	}

	@Override
	public void write(Writer val) throws IOException {
		
		//check to see whether to negate... 
		if(negated) {
			val.append("!(");
		}

		left.write(val);
		
		//close parentheses around negate...
		if(negated) {
			val.append(")");
		}
	}

	@Override
	public void negate() {
		this.negated = !negated;
	}
	
}

package org.candle.decompiler.intermediate.expression;

import org.apache.bcel.generic.InstructionHandle;

public abstract class ConditionalExpression extends Expression {

	public ConditionalExpression(InstructionHandle instructionHandle) {
		super(instructionHandle);
	}

	public abstract void negate();
}

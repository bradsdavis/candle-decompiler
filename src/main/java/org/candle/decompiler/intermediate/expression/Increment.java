package org.candle.decompiler.intermediate.expression;

import org.apache.bcel.generic.InstructionHandle;

public class Increment extends Resolved {

	private final Variable variable;
	
	public Increment(InstructionHandle instruction, Variable variable, String value) {
		super(instruction, value);
		this.variable = variable;
	}

	public Variable getVariable() {
		return variable;
	}
}

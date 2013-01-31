package org.candle.decompiler.intermediate.expression;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Type;

public class Increment extends Resolved {

	private final Variable variable;
	
	public Increment(InstructionHandle instruction, Variable variable, Type type, String value) {
		super(instruction, type, value);
		this.variable = variable;
	}

	public Variable getVariable() {
		return variable;
	}
}

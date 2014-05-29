package org.candle.decompiler.intermediate.expression;

import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.InstructionHandle;

public class Increment extends Resolved {

	private Variable variable;
	
	public Increment(InstructionHandle instruction, Variable variable, Type type, String value) {
		super(instruction, type, value);
		this.variable = variable;
	}

	public Variable getVariable() {
		return variable;
	}
	
	public void setVariable(Variable variable) {
		this.variable = variable;
	}
}

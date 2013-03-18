package org.candle.decompiler.intermediate.expression;

import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.InstructionHandle;

public class GeneratedVariable extends Variable {

	public GeneratedVariable(InstructionHandle instructionHandle, Type type, String name) {
		super(instructionHandle, type, name);
	}

}

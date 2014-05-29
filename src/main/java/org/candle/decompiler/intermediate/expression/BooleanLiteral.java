package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class BooleanLiteral extends Expression {

	private boolean value;
	
	public BooleanLiteral(InstructionHandle InstructionHandle, boolean value) {
		super(InstructionHandle);
		this.value = value;
	}

	public boolean isValue() {
		return value;
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.append(Boolean.toString(value));
	}

}

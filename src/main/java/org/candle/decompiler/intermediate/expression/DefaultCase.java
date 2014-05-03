package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;
 
public class DefaultCase extends Case {

	public DefaultCase(InstructionHandle instructionHandle) {
		super(instructionHandle, null);
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.append("default: ");
	}
}

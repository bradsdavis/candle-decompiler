package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class Throw extends Expression {

	private Expression throwing;
	
	public Throw(InstructionHandle handle, Expression target) {
		super(handle);
		this.throwing = target;
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		writer.append("throw ");
		throwing.write(writer);
	}
}

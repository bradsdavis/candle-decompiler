package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class Break extends Expression {

	public Break(InstructionHandle instructionHandle) {
		super(instructionHandle);
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.append("break");
	}
	
	@Override
	public void visit(ASTListener listener) {
		listener.accept(this);
	}

}

package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class NullLiteral extends Expression {

	public NullLiteral(InstructionHandle InstructionHandle) {
		super(InstructionHandle);
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.append("null");
	}

	@Override
	public void visit(ASTListener listener) {
		listener.accept(this);
	}

}

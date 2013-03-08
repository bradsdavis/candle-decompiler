package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class StatementBlock extends Expression {
	private final Expression left;
	private final Expression right;
	
	public StatementBlock(InstructionHandle instructionHandle, Expression left, Expression right) {
		super(instructionHandle);
		this.left = left;
		this.right = right;
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		left.write(writer);
		writer.append("\n");
		right.write(writer);
	}
}


package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.InstructionHandle;

public class Continue extends Expression {

	public Continue(InstructionHandle instructionHandle) {
		super(instructionHandle);
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.append("continue");
	}

	@Override
	public Set<Expression> nestedExpression() {
		return new HashSet<Expression>();
	}

}

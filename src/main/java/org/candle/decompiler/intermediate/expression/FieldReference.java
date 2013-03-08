package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;


public class FieldReference extends Expression {
	protected final Expression target;
	protected final String fieldName;

	public FieldReference(InstructionHandle instructionHandle, Expression target, String fieldName) {
		super(instructionHandle);
		this.target = target;
		this.fieldName = fieldName;
	}

	@Override
	public void write(Writer builder) throws IOException {
		target.write(builder);
		builder.append(".");
		builder.append(fieldName);
	}
	
}

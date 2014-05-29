package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class StringLiteral extends Expression {

	private String value;
	
	public StringLiteral(InstructionHandle InstructionHandle, String value) {
		super(InstructionHandle);
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.append("\""+value+"\"");
	}

}

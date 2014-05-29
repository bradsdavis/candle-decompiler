package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;

public class ByteLiteral extends Expression {

	private Number bytes;
	
	public ByteLiteral(InstructionHandle InstructionHandle, Number number) {
		super(InstructionHandle);
		this.bytes = number;
	}
	
	public Number getBytes() {
		return bytes;
	}
	
	public void setBytes(Number bytes) {
		this.bytes = bytes;
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.append(bytes.toString());
	}


}

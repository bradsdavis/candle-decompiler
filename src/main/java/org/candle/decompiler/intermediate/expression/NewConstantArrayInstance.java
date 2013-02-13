package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Type;

public class NewConstantArrayInstance extends NewArrayInstance {

	public NewConstantArrayInstance(InstructionHandle instructionHandle,
			Type type, Expression count) {
		super(instructionHandle, type, count);
	}
	
	@Override
	public void write(Writer builder) throws IOException {
		builder.write("CONSTANT ");
		super.write(builder);
	}
	
	@Override
	public String toString() {
		return "CONSTANT" + super.toString();
	}
	

}

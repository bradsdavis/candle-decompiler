package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;

import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.InstructionHandle;

public class NewMultidimensionalArray extends NewArrayInstance {

	protected final int dimensions;
	
	public NewMultidimensionalArray(InstructionHandle instructionHandle, Type type, Expression count, int dimensions) {
		super(instructionHandle, type, count);
		this.dimensions = dimensions;
	}

	
	@Override
	public void write(Writer builder) throws IOException {
		super.write(builder);
		
		for(int i=1; i<dimensions; i++) {
			builder.append("[]");
		}
	}
	
}

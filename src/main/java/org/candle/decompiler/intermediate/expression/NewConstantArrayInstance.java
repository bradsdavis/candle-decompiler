package org.candle.decompiler.intermediate.expression;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.InstructionHandle;

public class NewConstantArrayInstance extends NewArrayInstance {

	public NewConstantArrayInstance(InstructionHandle instructionHandle, Type type, Expression count) {
		super(instructionHandle, type, count);
	}
	
	public NewConstantArrayInstance(InstructionHandle instructionHandle, Type type, List<Expression> counts) {
		super(instructionHandle, type, counts);
	}
	
	public Expression getCount() {
		return counts.get(0);
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

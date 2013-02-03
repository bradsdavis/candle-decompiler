package org.candle.decompiler.intermediate.code.conditional;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.code.ConditionalIntermediate;
import org.candle.decompiler.intermediate.expression.ConditionalExpression;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class IfIntermediate extends ConditionalIntermediate {

	public IfIntermediate(InstructionHandle instruction,
			ConditionalExpression expression) {
		super(instruction, expression);
	}
	
	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		try {
			this.expression.write(sw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "If: "+sw.toString();
	}

	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitIfLine(this);
	}
	
}

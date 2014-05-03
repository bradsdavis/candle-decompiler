package org.candle.decompiler.intermediate.code;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.expression.Switch;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class MultiBranchIntermediate extends AbstractIntermediate {

	private final Switch expression;
	
	public MultiBranchIntermediate(InstructionHandle instruction, Switch expression) {
		super(instruction);
		this.expression = expression;
	}
	
	public Switch getExpression() {
		return expression;
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
		return "TestSwitch: "+sw.toString();
	}

	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitAbstractIntermediate(this);
		visitor.visitMultiBranchIntermediate(this);
	}
}

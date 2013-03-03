package org.candle.decompiler.intermediate.code.conditional;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.code.BlockRange;
import org.candle.decompiler.intermediate.code.BlockSerializable;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.expression.ConditionalExpression;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class IfIntermediate extends BooleanBranchIntermediate implements BlockSerializable {

	private BlockRange blockRange;
	
	public IfIntermediate(InstructionHandle instruction, ConditionalExpression expression) {
		super(instruction, expression);
		
		this.blockRange = new BlockRange();
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
		visitor.visitAbstractIntermediate(this);
		visitor.visitIfIntermediate(this);
	}

	@Override
	public BlockRange getBlockRange() {
		return this.blockRange;
	}
	
}

package org.candle.decompiler.intermediate.code.loop;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.code.BlockRange;
import org.candle.decompiler.intermediate.code.BlockSerializable;
import org.candle.decompiler.intermediate.code.BooleanBranchIntermediate;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class WhileIntermediate extends BooleanBranchIntermediate implements BlockSerializable {
	
	private final BlockRange blockRange;
	private BooleanBranchIntermediate conditionalIntermediate;
	
	public WhileIntermediate(InstructionHandle bi, BooleanBranchIntermediate ci) {
		super(bi, ci.getExpression());
		this.conditionalIntermediate = ci;
		this.blockRange = new BlockRange();
	}
	
	public BooleanBranchIntermediate getConditionalIntermediate() {
		return conditionalIntermediate;
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
		return "While: "+sw.toString();
	}
	
	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitAbstractLine(this);
		visitor.visitWhileLoopLine(this);
	}

	@Override
	public BlockRange getBlockRange() {
		return blockRange;
	}

}

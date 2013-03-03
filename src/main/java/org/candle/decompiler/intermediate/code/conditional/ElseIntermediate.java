package org.candle.decompiler.intermediate.code.conditional;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.code.AbstractIntermediate;
import org.candle.decompiler.intermediate.code.BlockRange;
import org.candle.decompiler.intermediate.code.BlockSerializable;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class ElseIntermediate extends AbstractIntermediate implements BlockSerializable {
	private BlockRange blockRange;
	
	public ElseIntermediate(InstructionHandle instruction) {
		super(instruction);
		this.blockRange = new BlockRange();
		this.blockRange.setStart(instruction);
	}
	
	@Override
	public String toString() {
		return "Else";
	}

	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitAbstractIntermediate(this);
		visitor.visitElseIntermediate(this);
	}

	@Override
	public BlockRange getBlockRange() {
		return this.blockRange;
	}

}

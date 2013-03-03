package org.candle.decompiler.intermediate.code;

import java.io.StringWriter;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class TryIntermediate extends AbstractIntermediate implements BlockSerializable {

	public TryIntermediate(InstructionHandle instruction) {
		super(instruction);
		this.blockRange = new BlockRange();
	}

	private final BlockRange blockRange;
	
	@Override
	public BlockRange getBlockRange() {
		return blockRange;
	}

	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitAbstractIntermediate(this);
		visitor.visitTryIntermediate(this);
	}

	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		sw.append("Try");
		return sw.toString() + ";";
	}
	

}

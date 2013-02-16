package org.candle.decompiler.intermediate.code;

import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public abstract class AbstractIntermediate {

	protected final InstructionHandle instruction;
	
	public AbstractIntermediate(InstructionHandle instruction) {
		this.instruction = instruction;
	}
	
	public InstructionHandle getInstruction() {
		return instruction;
	}
	
	public abstract void accept(IntermediateVisitor visitor);
}

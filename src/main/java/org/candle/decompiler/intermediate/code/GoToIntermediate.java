package org.candle.decompiler.intermediate.code;

import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class GoToIntermediate extends AbstractIntermediate {

	public GoToIntermediate(InstructionHandle instruction) {
		super(instruction);
	}

	@Override
	public void accept(IntermediateVisitor visitor) {
		visitor.visitAbstractIntermediate(this);
		visitor.visitGoToIntermediate(this);
	}

	@Override
	public String toString() {
		String t = null;
		
		Instruction i = ((InstructionHandle)getInstruction()).getInstruction();
		return "Goto ["+this.getInstruction().getPosition() + " -> " + t+"]";
	}
}

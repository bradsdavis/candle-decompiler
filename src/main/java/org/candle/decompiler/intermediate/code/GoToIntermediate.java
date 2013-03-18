package org.candle.decompiler.intermediate.code;

import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.candle.decompiler.intermediate.visitor.IntermediateVisitor;

public class GoToIntermediate extends AbstractIntermediate {

	private AbstractIntermediate target;
	
	public GoToIntermediate(InstructionHandle instruction) {
		super(instruction);
	}
	
	public AbstractIntermediate getTarget() {
		return target;
	}
	
	public void setTarget(AbstractIntermediate target) {
		this.target = target;
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
		
		if(this.target == null) {
			//t = "Unresolved Position: "+((BranchInstruction)(i).get.getTarget().getPosition();
		}
		else {
			t = target.toString();
		}
		
		return "Goto ["+this.getInstruction().getPosition() + " -> " + t+"]";
	}
}
